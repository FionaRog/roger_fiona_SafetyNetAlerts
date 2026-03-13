package com.openclassrooms.safetynetalerts.service.endpoint;

import com.openclassrooms.safetynetalerts.dto.FloodHouseholdDTO;
import com.openclassrooms.safetynetalerts.dto.FloodPersonDTO;
import com.openclassrooms.safetynetalerts.mapper.FloodMapper;
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
public class FloodServiceTest {

    @Mock
    private FloodMapper floodMapper;

    @InjectMocks
    private FloodService floodService;

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
    @DisplayName("Should return empty list when stations is null")
    void shouldReturnEmptyListWhenStationsIsNull() {

        List<FloodHouseholdDTO> result = floodService.getHouseholdsByStations(null);

        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Should return households for requested firestation")
    void shouldReturnHouseholdsForRequestedFirestation() {

        addFirestations(new Firestation("1509 Culver St", "3"));

        Person person = new Person("John", "Boyd", "1509 Culver St", "841-874-6512");
        addPersons(person);

        MedicalRecord record =
                new MedicalRecord("John", "Boyd", "01/01/1980", List.of(), List.of());
        addMedicalRecords(record);

        FloodPersonDTO floodPersonDTO = new FloodPersonDTO();
        floodPersonDTO.setFirstName("John");
        floodPersonDTO.setLastName("Boyd");
        floodPersonDTO.setPhone("841-874-6512");

        when(floodMapper.toFloodPersonDTO(any(), any(), anyInt()))
                .thenReturn(floodPersonDTO);

        List<FloodHouseholdDTO> result =
                floodService.getHouseholdsByStations(List.of("3"));

        assertEquals(1, result.size());

        FloodHouseholdDTO household = result.get(0);
        assertEquals("1509 culver st", household.getAddress());
        assertEquals(1, household.getResidents().size());

        verify(floodMapper).toFloodPersonDTO(any(), any(), anyInt());
    }
}
