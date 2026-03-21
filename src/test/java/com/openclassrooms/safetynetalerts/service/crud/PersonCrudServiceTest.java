package com.openclassrooms.safetynetalerts.service.crud;

import com.openclassrooms.safetynetalerts.repository.DataPersistenceService;
import org.mockito.Mockito;
import com.openclassrooms.safetynetalerts.model.Person;
import com.openclassrooms.safetynetalerts.repository.DataLoader;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.junit.jupiter.api.Assertions.*;

public class PersonCrudServiceTest {

    private PersonCrudService personCrudService;
    private DataPersistenceService dataPersistenceService;

    @BeforeEach
    void setUp() {
        DataLoader.PERSONS.clear();
        dataPersistenceService = Mockito.mock(DataPersistenceService.class);
        personCrudService = new PersonCrudService(dataPersistenceService);    }

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

        verify(dataPersistenceService).saveData();
    }

    @Test
    @DisplayName("Should reject person when duplicate already exists")
    void shouldRejectPersonWhenDuplicateAlreadyExists() {

        addPersons(new Person("John", "Boyd", "1509 Culver St"));

        Person duplicate = new Person(" John ", " BOYD ", "29 15th St");

        boolean result = personCrudService.addPerson(duplicate);

        assertFalse(result);
        assertEquals(1, DataLoader.PERSONS.size());

        verify(dataPersistenceService, never()).saveData();
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

        verify(dataPersistenceService).saveData();
    }

    @Test
    @DisplayName("Should delete person when matching person exists")
    void shouldDeletePersonWhenMatchingPersonExists() {

        Person person = new Person("John", "Boyd", "1509 Culver St");
        addPersons(person);

        boolean result = personCrudService.deletePerson("John", "Boyd");

        assertTrue(result);
        assertTrue(DataLoader.PERSONS.isEmpty());

        verify(dataPersistenceService).saveData();
    }
}
