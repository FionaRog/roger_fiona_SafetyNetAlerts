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

        if (firestation == null || firestation.isBlank()) {
            logger.debug("PhoneAlert rejected: firestation is null/blank");
            return List.of();
        }

        Set<String> coveredAdresses = new HashSet<>();

        for (Firestation fs : DataLoader.DATASOURCE.getFirestations()) {
            if (fs.getStation() !=null && fs.getStation().trim().equals(firestation)) {
                if(fs.getAddress() !=null && !fs.getAddress().isBlank()) {
                    coveredAdresses.add(normalize(fs.getAddress()));
                }
            }
        }
        logger.debug("PhoneAlert: {} addresses covered found",
                coveredAdresses.size());

        if (coveredAdresses.isEmpty()) {
            logger.debug("PhoneAlert: no addresses found");
            return List.of();
        }

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
        logger.debug("PhoneAlert: {} phone numbers collected", uniquePhones.size());
        return new ArrayList<>(uniquePhones);
    }
    private String normalize(String value) {
        return value.trim().toLowerCase();
    }
}
