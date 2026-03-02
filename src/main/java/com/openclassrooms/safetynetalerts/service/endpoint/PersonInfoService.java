package com.openclassrooms.safetynetalerts.service.endpoint;

import com.openclassrooms.safetynetalerts.dto.PersonInfoDTO;
import com.openclassrooms.safetynetalerts.mapper.PersonInfoMapper;
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
public class PersonInfoService implements IPersonInfoService {

    private static final Logger logger = LoggerFactory.getLogger(PersonInfoService.class);

    private final PersonInfoMapper personInfoMapper;

    public PersonInfoService(PersonInfoMapper personInfoMapper) {
        this.personInfoMapper = personInfoMapper;
    }

    public List<PersonInfoDTO> getPersonsByLastName(String lastName) {
        logger.debug("PersonInfo request: lastName={}", lastName);

        // Récupérer les infos de Person via lastName
        List<Person> matchedPersons = findPersonsByLastName(lastName);
        logger.debug("PersonInfo: {} persons found for lastName {}", matchedPersons.size(), lastName);

        if(matchedPersons.isEmpty()) {
            logger.debug("PersonInfo : no persons found for lastName {}", lastName);
            return List.of();
        }

        Map<String, MedicalRecord> medicalIndex = buildMedicalIndex();
        logger.debug("Person Info: medical index built with {} records", medicalIndex.size());


        List<PersonInfoDTO> personInfos = new ArrayList<>();

        for (Person p : matchedPersons) {
            if (p == null) continue;

            MedicalRecord mr = medicalIndex.get(personKey(p.getFirstName(), p.getLastName()));
            int age = resolveAge(p, mr);

            PersonInfoDTO dto = personInfoMapper.toPersonInfoDto(p, mr, age);
            if (dto != null) {
                personInfos.add(dto);
            }
        }
        logger.debug("PersonInfo : response built with {} persons", personInfos.size());
        return personInfos;
    }
// ------------------------ HELPERS ---------------------------------
    private Map<String, MedicalRecord> buildMedicalIndex() {
        Map<String, MedicalRecord> medicalIndex = new HashMap<>();

        for (MedicalRecord mr : DataLoader.DATASOURCE.getMedicalrecords()) {
            String key = personKey(mr.getFirstName(), mr.getLastName());
            medicalIndex.put(key, mr);
        }
        return medicalIndex;
    }

    private List<Person> findPersonsByLastName(String lastName) {
        if (lastName == null || lastName.isBlank()) {
            logger.debug("PersonInfo rejected: lastName is null/blank");
            return List.of();
        }
        List<Person> result = new ArrayList<>();

        for (Person p : DataLoader.DATASOURCE.getPersons()) {
            if (p == null || p.getLastName() == null) continue;

            if (normalize(p.getLastName()).equals(normalize(lastName))) {
                result.add(p);
            }
        }
        return result;
    }

    private int resolveAge(Person p, MedicalRecord mr) {
        if (mr == null || mr.getBirthdate() == null || mr.getBirthdate().isBlank()) {
            return 0;
        }
        try {
            return AgeUtils.calculateAge(mr.getBirthdate());
        } catch (Exception e) {
            logger.error("PersonInfo: cannot parse birthdate='{}' for {} {}",
                    mr.getBirthdate(), p.getFirstName(), p.getLastName(), e);
            return 0;
        }
    }

    private String normalize(String value) {
        return value == null ? "" : value.trim().toLowerCase();
    }

    private String personKey(String firstName, String lastName) {
        return (firstName == null ? "" : normalize(firstName))
                + "|"
                + (lastName == null ? "" : normalize(lastName));

    }
}