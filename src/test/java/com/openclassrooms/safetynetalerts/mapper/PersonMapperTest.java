package com.openclassrooms.safetynetalerts.mapper;

import com.openclassrooms.safetynetalerts.dto.AdultDTO;
import com.openclassrooms.safetynetalerts.dto.ChildAlertDTO;
import com.openclassrooms.safetynetalerts.model.Person;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PersonMapperTest {

    private final PersonMapper personMapper = new PersonMapperImpl();

    @Test
    @DisplayName("Should map person to adult and child DTO")
    void shouldMapPersonToAdultAndChildDto() {

        Person person = new Person("John", "Boyd", "1509 Culver St", "841-874-6512");

        AdultDTO adultDto = personMapper.toAdultDto(person);
        ChildAlertDTO childDto = personMapper.toChildAlertDto(person);

        assertEquals("John", adultDto.getFirstName());
        assertEquals("Boyd", adultDto.getLastName());

        assertEquals("John", childDto.getFirstName());
        assertEquals("Boyd", childDto.getLastName());
        assertEquals(0, childDto.getAge());
        assertTrue(childDto.getHouseholdMembers().isEmpty());
    }
}
