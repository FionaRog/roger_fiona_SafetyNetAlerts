package com.openclassrooms.safetynetalerts.mapper;

import com.openclassrooms.safetynetalerts.dto.FirestationPersonDTO;
import com.openclassrooms.safetynetalerts.model.Person;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class FirestationMapperTest {

    private final FirestationMapper firestationMapper = new FirestationMapperImpl();

    @Test
    @DisplayName("Should map person and person list to FirestationPersonDTO")
    void shouldMapPersonAndPersonListToFirestationPersonDTO() {

        Person person = new Person("John", "Boyd", "1509 Culver St", "841-874-6512");

        FirestationPersonDTO dto = firestationMapper.toFirestationPersonDTO(person);

        assertEquals("John", dto.getFirstName());
        assertEquals("Boyd", dto.getLastName());
        assertEquals("1509 Culver St", dto.getAddress());
        assertEquals("841-874-6512", dto.getPhone());

        List<FirestationPersonDTO> dtos =
                firestationMapper.toFirestationPersonDTOList(List.of(person));

        assertEquals(1, dtos.size());
        assertEquals("John", dtos.get(0).getFirstName());
    }
}
