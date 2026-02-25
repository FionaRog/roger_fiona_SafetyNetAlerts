package com.openclassrooms.safetynetalerts.service.crud;

import com.openclassrooms.safetynetalerts.model.Person;

import java.util.List;

public interface IPersonCrudService {

    List<Person> getPerson();

    boolean addPerson(Person newPerson);

    boolean updatePerson(Person updatedPerson);

    boolean deletePerson(String firstName, String lastName);
}
