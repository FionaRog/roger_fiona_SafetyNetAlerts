package com.openclassrooms.safetynetalerts.service.endpoint;

import com.openclassrooms.safetynetalerts.model.Firestation;
import com.openclassrooms.safetynetalerts.model.Person;
import com.openclassrooms.safetynetalerts.repository.DataLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Service
public class PhoneAlertService implements IPhoneAlertService {

    private static final Logger logger = LoggerFactory.getLogger(PhoneAlertService.class);


    public List<String> getPhoneByFirestation(String firestation) {
        logger.debug("PhoneAlert request: firestation='{}'", firestation);

        if (firestation == null || firestation.isBlank()) {
            logger.debug("PhoneAlert rejected: firestation is null/blank");
            return List.of();
        }

        Set<String> coveredAdresses = findCoveredAddressesByStation(firestation);
        logger.debug("PhoneAlert: {} addresses covered found for station '{}'",
                coveredAdresses.size(), firestation);

        if (coveredAdresses.isEmpty()) {
            logger.debug("PhoneAlert: no addresses found");
            return List.of();
        }

        Set<String> uniquePhones = collectPhonesByAddress(coveredAdresses);
        logger.debug("PhoneAlert: {} phone numbers collected for station '{}'", uniquePhones.size(), firestation);
        return new ArrayList<>(uniquePhones);
    }


    // ----------------------- HELPERS ---------------------------------
    private Set<String> findCoveredAddressesByStation(String firestation) {
        Set<String> coveredAdresses = new HashSet<>();

        for (Firestation fs : DataLoader.DATASOURCE.getFirestations()) {
            if (fs.getStation() !=null && normalize(fs.getStation()).equals(normalize(firestation))) {
                if(fs.getAddress() !=null && !fs.getAddress().isBlank()) {
                    coveredAdresses.add(normalize(fs.getAddress()));
                }
            }
        }
        return coveredAdresses;
    }

    private Set<String> collectPhonesByAddress(Set<String> coveredAdresses) {
        Set<String> uniquePhones = new HashSet<>();

        for(Person person : DataLoader.DATASOURCE.getPersons()) {
            if (person.getAddress() !=null) {
                String personAdress = normalize(person.getAddress());
                if (coveredAdresses.contains(personAdress)) {
                    if(person.getPhone() !=null && !person.getPhone().isBlank()) {
                        uniquePhones.add(person.getPhone().trim());
                    }
                }
            }
        }
        return uniquePhones;
    }

    private String normalize(String value) {
        return value.trim().toLowerCase();
    }
}
