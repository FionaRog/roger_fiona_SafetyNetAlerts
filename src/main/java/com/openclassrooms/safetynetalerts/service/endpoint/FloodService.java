package com.openclassrooms.safetynetalerts.service.endpoint;

import com.openclassrooms.safetynetalerts.dto.FloodHouseholdDTO;
import com.openclassrooms.safetynetalerts.dto.FloodPersonDTO;
import com.openclassrooms.safetynetalerts.mapper.FloodMapper;
import com.openclassrooms.safetynetalerts.model.Firestation;
import com.openclassrooms.safetynetalerts.model.MedicalRecord;
import com.openclassrooms.safetynetalerts.model.Person;
import com.openclassrooms.safetynetalerts.repository.DataLoader;
import com.openclassrooms.safetynetalerts.utils.AgeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class FloodService implements IFloodService{

    private static final Logger logger = LoggerFactory.getLogger(FloodService.class);

    private final FloodMapper floodMapper;

    public FloodService (FloodMapper floodMapper) {
        this.floodMapper = floodMapper;
    }


    public List<FloodHouseholdDTO> getHouseholdsByStations(List<String> stations) {
        logger.debug("Flood request: stations={}", stations);

        if (stations == null || stations.isEmpty()) {
            logger.debug("Flood rejected: stations is null/empty");
            return List.of();
        }

        Set<String> stationSet = new HashSet<>();
        for(String s : stations) {
            if (s == null || s.isBlank()) continue;
            stationSet.add(s.trim());
        }

        Set<String> coveredAddresses = findCoveredAddresses (stationSet);
        logger.debug("Flood: {} covered addresses found for stations={}", coveredAddresses.size(), stationSet);

        if(coveredAddresses.isEmpty()) {
            logger.debug("Flood: no covered addresses found");
            return List.of();
        }

        Map<String, MedicalRecord> medicalIndex = buildMedicalIndex();
        logger.debug("Flood: medical index built with {} records", medicalIndex.size());

        Map<String, List<Person>> personsByAddress = groupPersons(coveredAddresses);
        logger.debug("Flood: {} household(s) matched", personsByAddress.size());

        if(personsByAddress.isEmpty()) {
            return List.of();
        }

        List<FloodHouseholdDTO> households = new ArrayList<>();

        for(Map.Entry<String, List<Person>> entry : personsByAddress.entrySet()) {
            String addressKey = entry.getKey();
            List<Person> persons = entry.getValue();

            List<FloodPersonDTO> residents = buildResidents (persons, medicalIndex);

            FloodHouseholdDTO household = new FloodHouseholdDTO();
            household.setAddress(addressKey);
            household.setResidents(residents);

            households.add(household);
        }

        logger.debug("Flood: response built with {} household(s)", households.size());
        return households;
    }

// ----------------------- HELPERS ------------------------------

    private Set<String> findCoveredAddresses(Set<String> stationSet) {
        Set<String> coveredAddresses = new HashSet<>();

        List<Firestation> firestations = DataLoader.DATASOURCE.getFirestations();
        for (Firestation fs : firestations) {
            if (fs == null || fs.getStation() == null || fs.getAddress() == null) continue;

            String st = fs.getStation().trim();
            if (!stationSet.contains(st)) continue;

            if (!fs.getAddress().isBlank()) {
                coveredAddresses.add(normalize(fs.getAddress()));
            }
        }
        return coveredAddresses;
    }

    private Map<String, MedicalRecord> buildMedicalIndex() {
        Map<String, MedicalRecord> medicalIndex = new HashMap<>();

        for(MedicalRecord mr : DataLoader.DATASOURCE.getMedicalrecords()) {
            String key = personKey(mr.getFirstName(), mr.getLastName());
            medicalIndex.put(key, mr);
        }
        return medicalIndex;
    }

    private Map<String, List<Person>> groupPersons(Set<String> coveredAddresses) {
        Map<String, List<Person>> result = new HashMap<>();

        for(Person p : DataLoader.DATASOURCE.getPersons()) {
            if (p == null || p.getAddress() == null) continue;

            String addrKey = normalize(p.getAddress());
            if (!coveredAddresses.contains(addrKey)) continue;

            if (!result.containsKey(addrKey)) {
                result.put(addrKey, new ArrayList<>());
            }
            result.get(addrKey).add(p);
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
            logger.error("Flood: cannot parse birthdate='{}' for {} {}",
                    mr.getBirthdate(), p.getFirstName(), p.getLastName(), e);
            return 0;
        }
    }

    private List<FloodPersonDTO> buildResidents(List<Person> persons, Map<String, MedicalRecord> medicalIndex) {
        List<FloodPersonDTO> residents = new ArrayList<>();

        for(Person p : persons) {
            if (p == null) continue;

            MedicalRecord mr = medicalIndex.get(personKey(p.getFirstName(), p.getLastName()));
            int age = resolveAge(p, mr);

            FloodPersonDTO dto = floodMapper.toFloodPersonDTO(p, mr, age);
            if(dto !=null) {
                residents.add(dto);
            }
        }
        return residents;
    }

    private String personKey(String firstName, String lastName) {
        return (firstName == null ? "" : normalize(firstName))
                + "|"
                + (lastName == null ? "" : normalize(lastName));
    }

    private String normalize(String value) {
        return value.trim().toLowerCase();
    }
}
