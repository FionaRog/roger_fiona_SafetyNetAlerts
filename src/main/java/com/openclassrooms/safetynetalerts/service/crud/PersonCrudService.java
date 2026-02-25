package com.openclassrooms.safetynetalerts.service.crud;

import com.openclassrooms.safetynetalerts.model.Person;
import com.openclassrooms.safetynetalerts.repository.DataLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PersonCrudService implements IPersonCrudService {

    private static final Logger logger = LoggerFactory.getLogger(PersonCrudService.class);

    public List<Person> getPerson() {
        return new ArrayList<>(DataLoader.DATASOURCE.getPersons());
    }


    public boolean addPerson(Person newPerson) {

        for(Person person : DataLoader.DATASOURCE.getPersons()) {
            if(person.getFirstName().equalsIgnoreCase(newPerson.getFirstName())
                && person.getLastName().equalsIgnoreCase(newPerson.getLastName())) {
                logger.debug("Add person rejected : duplicate for {} {} ",
                        newPerson.getFirstName(), newPerson.getLastName());
                return false;
            }
        }

        DataLoader.DATASOURCE.getPersons().add(newPerson);

        logger.debug("new person {} {} added ", newPerson.getFirstName(), newPerson.getLastName());
        return true;
    }

    public boolean updatePerson(Person updatedPerson) {

        for (Person person : DataLoader.DATASOURCE.getPersons()) {
            if(person.getFirstName().equalsIgnoreCase(updatedPerson.getFirstName())
                    && person.getLastName().equalsIgnoreCase(updatedPerson.getLastName())) {

                person.setAddress(updatedPerson.getAddress());
                person.setCity(updatedPerson.getCity());
                person.setZip(updatedPerson.getZip());
                person.setPhone(updatedPerson.getPhone());
                person.setEmail(updatedPerson.getEmail());

                logger.debug("person {} {} updated",
                        person.getFirstName(), person.getLastName());
                return true;
            }
        }

        logger.debug("Update person for {} {} rejected, not found",
                updatedPerson.getFirstName(), updatedPerson.getLastName());
        return false;
    }

    public boolean deletePerson(String firstName, String lastName) {

        if(firstName == null || firstName.isBlank() || lastName == null || lastName.isBlank()) {
            logger.debug("Delete person rejected : firstName/LastName is null/blank");
            return false;
        }
        int before = DataLoader.DATASOURCE.getPersons().size();

        DataLoader.DATASOURCE.getPersons().removeIf(p ->
                p.getFirstName() != null
                && p.getLastName() != null
                && p.getFirstName().equalsIgnoreCase(firstName)
                && p.getLastName().equalsIgnoreCase(lastName)
        );

        int after = DataLoader.DATASOURCE.getPersons().size();

        boolean deleted = after < before;

        if (deleted) {
            logger.debug("person {} {} deleted", firstName, lastName);
        } else {
            logger.debug("Delete person {} {} rejected", firstName, lastName);
        }

        return deleted;
    }
}
