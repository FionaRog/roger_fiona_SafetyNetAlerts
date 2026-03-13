package com.openclassrooms.safetynetalerts.service.endpoint;

import com.openclassrooms.safetynetalerts.model.Firestation;
import com.openclassrooms.safetynetalerts.model.Person;
import com.openclassrooms.safetynetalerts.repository.DataLoader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PhoneAlertServiceTest {

    private PhoneAlertService phoneAlertService;

    @BeforeEach
    void setUp() {
        DataLoader.PERSONS.clear();
        DataLoader.MEDICAL_RECORDS.clear();
        DataLoader.FIRESTATIONS.clear();

        phoneAlertService = new PhoneAlertService();
    }

    private void addPersons(Person... persons) {
        DataLoader.PERSONS.addAll(List.of(persons));
    }

    private void addFirestations(Firestation... firestations) {
        DataLoader.FIRESTATIONS.addAll(List.of(firestations));
    }

    @Test
    @DisplayName("Should return empty list when firestation is null")
    void shouldReturnEmptyListWhenFirestationIsNull() {
        List<String> result = phoneAlertService.getPhoneByFirestation(null);

        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Should return phone numbers for persons covered by firestation")
    void shouldReturnPhoneNumbersForPersonsCoveredByFirestation() {

        Firestation firestation = new Firestation("1509 Culver St", "3");
        addFirestations(firestation);

        Person person1 = new Person("John", "Boyd", "1509 Culver St","841-874-6512");

        Person person2 = new Person("Jacob", "Boyd", "1509 Culver St","841-874-8547");

        addPersons(person1, person2);

        List<String> result = phoneAlertService.getPhoneByFirestation("3");

        assertEquals(2, result.size());
        assertTrue(result.contains("841-874-6512"));
        assertTrue(result.contains("841-874-8547"));
    }

    @Test
    @DisplayName("Should return unique phone numbers and ignore blank phones")
    void shouldReturnUniquePhoneNumbersAndIgnoreBlankPhones() {

        Firestation firestation = new Firestation("1509 culver St ", "3");
        addFirestations(firestation);

        Person person1 = new Person("John", "Boyd", "1509 Culver St", "841-874-6512");
        Person person2 = new Person("Jacob", "Boyd", "1509 Culver St", "841-874-6512");
        Person person3 = new Person("Julie", "Boyd", "1509 Culver St", "   ");

        addPersons(person1, person2, person3);

        List<String> result = phoneAlertService.getPhoneByFirestation("3");

        assertEquals(1, result.size());
        assertTrue(result.contains("841-874-6512"));
    }

    @Test
    @DisplayName("Should return empty list when firestation is blank")
    void shouldReturnEmptyListWhenFirestationIsBlank() {
        List<String> result = phoneAlertService.getPhoneByFirestation("   ");

        assertTrue(result.isEmpty());
    }
}
