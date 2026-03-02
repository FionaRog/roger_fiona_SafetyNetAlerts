package com.openclassrooms.safetynetalerts.service.endpoint;

import com.openclassrooms.safetynetalerts.dto.AdultDTO;
import com.openclassrooms.safetynetalerts.dto.ChildAlertDTO;
import com.openclassrooms.safetynetalerts.mapper.PersonMapper;
import com.openclassrooms.safetynetalerts.model.MedicalRecord;
import com.openclassrooms.safetynetalerts.model.Person;
import com.openclassrooms.safetynetalerts.repository.DataLoader;
import com.openclassrooms.safetynetalerts.utils.AgeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ChildAlertService implements IChildAlertService{


    private static final Logger logger = LoggerFactory.getLogger(ChildAlertService.class);

    private final PersonMapper personMapper;

    public ChildAlertService(PersonMapper personMapper) {
        this.personMapper = personMapper;
    }


     public List<ChildAlertDTO> getChildByAddress (String address) {
         logger.debug("ChildAlert request: address='{}'", address);

        if(address == null || address.isBlank()) {
            logger.debug("ChildAlert rejected: address is null/blank");
            return List.of();
        }

        String targetAddress = normalize(address);

        Map<String, MedicalRecord> medicalIndex = buildMedicalIndex();

         List<Person> household = findHouseholdByAddress(targetAddress);
         logger.debug("ChildAlert: household size for address '{}' = {}",
                 targetAddress, household.size());

        if (household.isEmpty()) {
            logger.debug("ChildAlert: no household found for address '{}'", targetAddress);
            return List.of();
        }

        List<AdultDTO> adults = new ArrayList<>();
        List<ChildAlertDTO> children = new ArrayList<>();

        for (Person p : household) {
            MedicalRecord mr = medicalIndex.get(personKey(p.getFirstName(),p.getLastName()));

            Integer age = resolveAge(p, mr);
            if (age == null) continue;

            if (age <= 18) {
                ChildAlertDTO child = personMapper.toChildAlertDto(p);
                child.setAge(age);
                children.add(child);
            } else {
                AdultDTO adult = personMapper.toAdultDto(p);
                adults.add(adult);
            }
        }

         logger.debug("ChildAlert: {} children and {} adults found for address '{}'",
                 children.size(), adults.size(), targetAddress);

        if (children.isEmpty()) {
            logger.debug("ChildAlert: no children found for address '{}'", targetAddress);
            return List.of();
        }

        for(ChildAlertDTO child : children) {
            child.setHouseholdMembers(new ArrayList<>(adults));
        }

        return children;
    }

// --------------------- HELPERS -------------------------------
    private String normalize(String value) {
        return value.trim().toLowerCase();
    }

    private String personKey (String firstName, String lastName) {
        return (firstName == null ? "" : normalize(firstName))
                + "|"
                + (lastName == null ? "" : normalize(lastName));
    }

    private Map<String, MedicalRecord> buildMedicalIndex() {

        Map<String, MedicalRecord> index = new HashMap<>();

        for (MedicalRecord mr : DataLoader.DATASOURCE.getMedicalrecords()) {
            index.put(personKey(mr.getFirstName(), mr.getLastName()), mr);
        }

        logger.debug("ChildAlert: medical index built with {} records", index.size());

        return index;
    }

    private List<Person> findHouseholdByAddress(String targetAddress) {
        List<Person> household = new ArrayList<>();

        for (Person p : DataLoader.DATASOURCE.getPersons()) {
            if(p.getAddress() == null) continue;
            if(normalize(p.getAddress()).equals(targetAddress)) {
                household.add(p);
            }
        }
        return household;
    }

    private Integer resolveAge(Person p, MedicalRecord mr) {
        if (mr == null || mr.getBirthdate() == null || mr.getBirthdate().isBlank()) {
            logger.debug("ChildAlert : no birthdate for {} {}", p.getFirstName(), p.getLastName());
            return null;
        }
        try {
            return AgeUtils.calculateAge(mr.getBirthdate());
        } catch (IllegalArgumentException e) {
            logger.error("ChildAlert: invalid birthdate '{}' for {} {}",
                    mr.getBirthdate(), p.getFirstName(), p.getLastName(), e);
            return null;
        }
    }
}
