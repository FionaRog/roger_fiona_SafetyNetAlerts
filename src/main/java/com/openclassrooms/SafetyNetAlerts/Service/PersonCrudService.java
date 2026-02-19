package com.openclassrooms.SafetyNetAlerts.Service;

import com.openclassrooms.SafetyNetAlerts.Model.Person;
import org.springframework.stereotype.Service;

@Service
public class PersonCrudService {
    public boolean addPerson(Person newPerson) {

        //Si la personne existe déjà , retourner faux
        for(Person person : DataRunner.DATASOURCE.getPersons()) {
            if(person.getFirstName().equalsIgnoreCase(newPerson.getFirstName())
                && person.getLastName().equalsIgnoreCase(newPerson.getLastName())) {
                return false;
            }
        }

        DataRunner.DATASOURCE.getPersons().add(newPerson);
        return true;
    }

    public boolean updatePerson(Person updatedPerson) {

        for (Person person : DataRunner.DATASOURCE.getPersons()) {
            if(person.getFirstName().equalsIgnoreCase(updatedPerson.getFirstName())
                    && person.getLastName().equalsIgnoreCase(updatedPerson.getLastName())) {

                person.setAddress(updatedPerson.getAddress());
                person.setCity(updatedPerson.getCity());
                person.setZip(updatedPerson.getZip());
                person.setPhone(updatedPerson.getPhone());
                person.setEmail(updatedPerson.getEmail());

                return true;
            }
        }

        return false;
    }

    //Changer pour removeIf
    public boolean deletePerson(String firstName, String lastName) {

        for(Person person : DataRunner.DATASOURCE.getPersons()) {
            if (person.getFirstName().equalsIgnoreCase(firstName)
                    && person.getLastName().equalsIgnoreCase(lastName)) {

                DataRunner.DATASOURCE.getPersons().remove(person);
                return true;
            }
        }

            return false;

    }
}
