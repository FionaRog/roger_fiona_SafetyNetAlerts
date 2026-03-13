package com.openclassrooms.safetynetalerts.service.crud;

import com.openclassrooms.safetynetalerts.model.Person;
import com.openclassrooms.safetynetalerts.repository.DataLoader;
import com.openclassrooms.safetynetalerts.service.crud.PersonCrudService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PersonCrudServiceTest {

    private PersonCrudService personCrudService;

    @BeforeEach
    void setUp() {
        DataLoader.PERSONS.clear();
        personCrudService = new PersonCrudService();
    }

    private void addPersons(Person... persons) {
        DataLoader.PERSONS.addAll(List.of(persons));
    }

    @Test
    @DisplayName("Should add person when no duplicate exists")
    void shouldAddPersonWhenNoDuplicateExists() {

        Person newPerson = new Person("John", "Boyd", "1509 Culver St");

        boolean result = personCrudService.addPerson(newPerson);

        assertTrue(result);
        assertEquals(1, DataLoader.PERSONS.size());
        assertEquals("John", DataLoader.PERSONS.get(0).getFirstName());
    }

    @Test
    @DisplayName("Should reject person when duplicate already exists")
    void shouldRejectPersonWhenDuplicateAlreadyExists() {

        addPersons(new Person("John", "Boyd", "1509 Culver St"));

        Person duplicate = new Person(" John ", " BOYD ", "29 15th St");

        boolean result = personCrudService.addPerson(duplicate);

        assertFalse(result);
        assertEquals(1, DataLoader.PERSONS.size());
    }

    @Test
    @DisplayName("Should update person when matching person exists")
    void shouldUpdatePersonWhenMatchingPersonExists() {

        Person existing = new Person("John", "Boyd", "1509 Culver St");
        addPersons(existing);

        Person updated = new Person("John", "Boyd", "29 15th St");

        boolean result = personCrudService.updatePerson(updated);

        assertTrue(result);
        assertEquals("29 15th St", DataLoader.PERSONS.get(0).getAddress());
    }

    @Test
    @DisplayName("Should delete person when matching person exists")
    void shouldDeletePersonWhenMatchingPersonExists() {

        Person person = new Person("John", "Boyd", "1509 Culver St");
        addPersons(person);

        boolean result = personCrudService.deletePerson("John", "Boyd");

        assertTrue(result);
        assertTrue(DataLoader.PERSONS.isEmpty());
    }
}
