package com.openclassrooms.safetynetalerts.mapper;

import com.openclassrooms.safetynetalerts.dto.FloodPersonDTO;
import com.openclassrooms.safetynetalerts.model.MedicalRecord;
import com.openclassrooms.safetynetalerts.model.Person;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class FloodMapperTest {

    private final FloodMapper floodMapper = new FloodMapperImpl();

    @Test
    @DisplayName("Should map person and medical record to FloodPersonDTO")
    void shouldMapPersonAndMedicalRecordToFloodPersonDTO() {

        Person person = new Person("John", "Boyd", "1509 Culver St", "841-874-6512");

        MedicalRecord record = new MedicalRecord(
                "John",
                "Boyd",
                "01/01/1980",
                List.of("med1"),
                List.of("allergy1")
        );

        FloodPersonDTO dto = floodMapper.toFloodPersonDTO(person, record, 45);

        assertEquals("John", dto.getFirstName());
        assertEquals("Boyd", dto.getLastName());
        assertEquals("841-874-6512", dto.getPhone());
        assertEquals(45, dto.getAge());
        assertEquals(List.of("med1"), dto.getMedications());
        assertEquals(List.of("allergy1"), dto.getAllergies());
    }
}
