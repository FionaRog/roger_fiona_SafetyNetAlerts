package com.openclassrooms.safetynetalerts.service.endpoint;

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
public class CommunityEmailService implements ICommunityEmailService{

    private static final Logger logger = LoggerFactory.getLogger(CommunityEmailService.class);

    public List<String> getEmailsByCity(String city) {
            if (city == null || city.isBlank()) {
                logger.debug("CommunityEmail rejected: city is null/blank");
                return List.of();
        }

        Set<String> uniqueEmails = new HashSet<>();

        for (Person person : DataLoader.DATASOURCE.getPersons()) {

            if (person.getCity() != null && person.getCity().equalsIgnoreCase(city)) {

                if (person.getEmail() != null && !person.getEmail().isBlank()) {
                    uniqueEmails.add(person.getEmail());
                }
            }
        }
        logger.debug("CommunityEmail: {} emails returned for city '{}'",
               uniqueEmails.size() , city);
        return new ArrayList<>(uniqueEmails);
    }
}
