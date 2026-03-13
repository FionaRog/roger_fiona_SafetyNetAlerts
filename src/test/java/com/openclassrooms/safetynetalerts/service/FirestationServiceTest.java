package com.openclassrooms.safetynetalerts.service;

import com.openclassrooms.safetynetalerts.dto.FirestationPersonDTO;
import com.openclassrooms.safetynetalerts.dto.FirestationResponseDTO;
import com.openclassrooms.safetynetalerts.mapper.FirestationMapper;
import com.openclassrooms.safetynetalerts.model.Firestation;
import com.openclassrooms.safetynetalerts.model.MedicalRecord;
import com.openclassrooms.safetynetalerts.model.Person;
import com.openclassrooms.safetynetalerts.repository.DataLoader;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FirestationServiceTest {

    @Mock
    private FirestationMapper firestationMapper;

    private FirestationService firestationService;

    @BeforeEach
    void setUp() {
        DataLoader.PERSONS.clear();
        DataLoader.MEDICAL_RECORDS.clear();
        DataLoader.FIRESTATIONS.clear();
        firestationService = new FirestationService(firestationMapper);
    }

    private void addFirestations(Firestation... firestations) {
        DataLoader.FIRESTATIONS.addAll(List.of(firestations));
    }

    @Test
    @DisplayName("Should add firestation mapping when it does not exist")
    void shouldAddFirestationMappingWhenItDoesNotExist() {

        Firestation mapping = new Firestation("1509 Culver St", "3");

        boolean result = firestationService.addFirestation(mapping);

        assertTrue(result);
        assertEquals(1, DataLoader.FIRESTATIONS.size());
    }

    @Test
    @DisplayName("Should reject firestation mapping when duplicate exists")
    void shouldRejectFirestationMappingWhenDuplicateExists() {

        addFirestations(new Firestation("1509 Culver St", "3"));

        Firestation duplicate = new Firestation("1509 Culver St", "3");

        boolean result = firestationService.addFirestation(duplicate);

        assertFalse(result);
        assertEquals(1, DataLoader.FIRESTATIONS.size());
    }

    @Test
    @DisplayName("Should update firestation when matching address exists")
    void shouldUpdateFirestationWhenMatchingAddressExists() {

        addFirestations(new Firestation("1509 Culver St", "3"));

        Firestation updated = new Firestation("1509 Culver St", "4");

        boolean result = firestationService.updateFirestationByAddress(updated);

        assertTrue(result);
        assertEquals("4", DataLoader.FIRESTATIONS.get(0).getStation());
    }

    @Test
    @DisplayName("Should delete firestation by address when matching address exists")
    void shouldDeleteFirestationByAddressWhenMatchingAddressExists() {

        addFirestations(new Firestation("1509 Culver St", "3"));

        boolean result = firestationService.deleteFirestationByAddress("1509 Culver St");

        assertTrue(result);
        assertTrue(DataLoader.FIRESTATIONS.isEmpty());
    }

    @Test
    @DisplayName("Should delete firestations by station when matching station exists")
    void shouldDeleteFirestationsByStationWhenMatchingStationExists() {

        addFirestations(
                new Firestation("1509 Culver St", "3"),
                new Firestation("29 15th St", "3")
        );

        boolean result = firestationService.deleteFirestationByStation("3");

        assertTrue(result);
        assertTrue(DataLoader.FIRESTATIONS.isEmpty());
    }

    @Test
    @DisplayName("Should return response when firestation exists")
    void shouldReturnResponseWhenFirestationExists() {

        addFirestations(new Firestation("1509 Culver St", "3"));

        DataLoader.PERSONS.add(new Person("John", "Boyd", "1509 Culver St"));

        DataLoader.MEDICAL_RECORDS.add(
                new MedicalRecord("John", "Boyd", "01/01/1980")
        );

        FirestationPersonDTO personDTO = new FirestationPersonDTO();
        personDTO.setFirstName("John");
        personDTO.setLastName("Boyd");

        when(firestationMapper.toFirestationPersonDTO(any(Person.class)))
                .thenReturn(personDTO);

        FirestationResponseDTO result = firestationService.getPersonsByFirestation("3");

        assertEquals(1, result.getAdults());
        assertEquals(0, result.getChildren());
        assertEquals(1, result.getPeople().size());

        verify(firestationMapper).toFirestationPersonDTO(any(Person.class));
    }
}
