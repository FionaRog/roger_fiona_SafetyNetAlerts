package com.openclassrooms.safetynetalerts.service.endpoint;

import com.openclassrooms.safetynetalerts.model.Person;
import com.openclassrooms.safetynetalerts.repository.DataLoader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CommunityEmailServiceTest {

    private CommunityEmailService communityEmailService;

    @BeforeEach
    void setUp() {
        DataLoader.PERSONS.clear();
        communityEmailService = new CommunityEmailService();
    }

    private void addPersons(Person... persons) {
        DataLoader.PERSONS.addAll(List.of(persons));
    }

    @Test
    @DisplayName("Should return empty list when city is null")
    void shouldReturnEmptyListWhenCityIsNull() {

        List<String> result = communityEmailService.getEmailsByCity(null);

        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Should return emails for persons living in the specified city")
    void shouldReturnEmailsForPersonsLivingInCity() {

        Person person1 = new Person("John", "Boyd", "1509 Culver St");
        person1.setCity("Culver");
        person1.setEmail("john@email.com");

        Person person2 = new Person("Jacob", "Boyd", "1509 Culver St");
        person2.setCity("Culver");
        person2.setEmail("jacob@email.com");

        addPersons(person1, person2);

        List<String> result = communityEmailService.getEmailsByCity("Culver");

        assertEquals(2, result.size());
        assertTrue(result.containsAll(List.of(
                "john@email.com",
                "jacob@email.com"
        )));
    }

    @Test
    @DisplayName("Should return unique emails and ignore blank emails")
    void shouldReturnUniqueEmailsAndIgnoreBlankEmails() {

        Person person1 = new Person("John", "Boyd", "1509 Culver St");
        person1.setCity("Culver");
        person1.setEmail("john@email.com");

        Person person2 = new Person("Jacob", "Boyd", "1509 Culver St");
        person2.setCity("Culver");
        person2.setEmail("john@email.com");

        Person person3 = new Person("Julie", "Boyd", "1509 Culver St");
        person3.setCity("Culver");
        person3.setEmail("   ");

        addPersons(person1, person2, person3);

        List<String> result = communityEmailService.getEmailsByCity("Culver");

        assertEquals(1, result.size());
        assertTrue(result.contains("john@email.com"));
    }
}
