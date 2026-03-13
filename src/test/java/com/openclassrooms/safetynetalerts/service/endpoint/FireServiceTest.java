package com.openclassrooms.safetynetalerts.service.endpoint;

import com.openclassrooms.safetynetalerts.dto.FirePersonDTO;
import com.openclassrooms.safetynetalerts.dto.FireResponseDTO;
import com.openclassrooms.safetynetalerts.mapper.FireMapper;
import com.openclassrooms.safetynetalerts.model.Firestation;
import com.openclassrooms.safetynetalerts.model.MedicalRecord;
import com.openclassrooms.safetynetalerts.model.Person;
import com.openclassrooms.safetynetalerts.repository.DataLoader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FireServiceTest {

    @Mock
    private FireMapper fireMapper;

    @InjectMocks
    private FireService fireService;

    @BeforeEach
    void setUp() {
        DataLoader.PERSONS.clear();
        DataLoader.MEDICAL_RECORDS.clear();
        DataLoader.FIRESTATIONS.clear();
    }

    private void addPersons(Person... persons) {
        DataLoader.PERSONS.addAll(List.of(persons));
    }

    private void addMedicalRecords(MedicalRecord... records) {
        DataLoader.MEDICAL_RECORDS.addAll(List.of(records));
    }

    private void addFirestations(Firestation... firestations) {
        DataLoader.FIRESTATIONS.addAll(List.of(firestations));
    }

    @Test
    @DisplayName("Should return empty list when address is null")
    void shouldReturnEmptyListWhenAddressIsNull() {

        FireResponseDTO result = fireService.getPersonByAddress(null);

        assertTrue(result.getFirePersonDtos().isEmpty());
    }

    @Test
    @DisplayName("Should return empty response when no station covers address")
    void shouldReturnEmptyResponseWhenNoStationCoversAddress() {

        addFirestations(new Firestation("29 15th St", "2"));

        FireResponseDTO result = fireService.getPersonByAddress("1509 Culver St");

        assertTrue(result.getFirePersonDtos().isEmpty());
    }

    @Test
    @DisplayName("Should return fire response for persons living at address")
    void shouldReturnFireResponseForPersonsLivingAtAddress() {

        Firestation firestation = new Firestation("1509 Culver St", "3");
        addFirestations(firestation);

        Person person = new Person("John", "Boyd", "1509 Culver St", "841-874-6512");
        addPersons(person);

        MedicalRecord record =
                new MedicalRecord("John", "Boyd", "01/01/1980", List.of(), List.of());

        addMedicalRecords(record);

        FirePersonDTO firePersonDTO = new FirePersonDTO();
        firePersonDTO.setFirstName("John");
        firePersonDTO.setLastName("Boyd");
        firePersonDTO.setPhone("841-874-6512");

        when(fireMapper.toFirePersonDTO(any(), any(), anyInt())).thenReturn(firePersonDTO);

        FireResponseDTO result = fireService.getPersonByAddress("1509 Culver St");

        assertEquals("3", result.getStation());
        assertEquals(1, result.getFirePersonDtos().size());

        verify(fireMapper).toFirePersonDTO(any(), any(), anyInt());
    }
}
