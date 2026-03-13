package com.openclassrooms.safetynetalerts.mapper;

import com.openclassrooms.safetynetalerts.dto.PersonInfoDTO;
import com.openclassrooms.safetynetalerts.model.MedicalRecord;
import com.openclassrooms.safetynetalerts.model.Person;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PersonInfoMapperTest {

    private final PersonInfoMapper personInfoMapper = new PersonInfoMapperImpl();

    @Test
    @DisplayName("Should map person and medical record to PersonInfoDTO")
    void shouldMapPersonAndMedicalRecordToPersonInfoDTO() {

        Person person = new Person("John", "Boyd", "1509 Culver St");
        person.setEmail("john@email.com");

        MedicalRecord record = new MedicalRecord(
                "John",
                "Boyd",
                "01/01/1980",
                List.of("med1"),
                List.of("allergy1")
        );

        PersonInfoDTO dto = personInfoMapper.toPersonInfoDto(person, record, 45);

        assertEquals("John", dto.getFirstName());
        assertEquals("Boyd", dto.getLastName());
        assertEquals("john@email.com", dto.getEmail());
        assertEquals(45, dto.getAge());
        assertEquals(List.of("med1"), dto.getMedications());
        assertEquals(List.of("allergy1"), dto.getAllergies());
    }
}
