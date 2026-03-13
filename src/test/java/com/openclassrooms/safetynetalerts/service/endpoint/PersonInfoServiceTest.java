package com.openclassrooms.safetynetalerts.service.endpoint;

import com.openclassrooms.safetynetalerts.dto.PersonInfoDTO;
import com.openclassrooms.safetynetalerts.mapper.PersonInfoMapper;
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
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PersonInfoServiceTest {

    @Mock
    private PersonInfoMapper personInfoMapper;

    @InjectMocks
    private PersonInfoService personInfoService;

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

    @Test
    @DisplayName("Should return empty list when lastName is null")
    void shouldReturnEmptyListWhenLastNameIsNull() {
        List<PersonInfoDTO> result = personInfoService.getPersonsByLastName(null);

        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Should return person info when lastName matches")
    void shouldReturnPersonInfoWhenLastNameMatches() {

        Person person = new Person("John", "Boyd", "1509 Culver St");
        person.setCity("Culver");
        person.setEmail("john@email.com");

        addPersons(person);

        MedicalRecord record =
                new MedicalRecord("John", "Boyd", "01/01/1980", List.of(), List.of());

        addMedicalRecords(record);

        PersonInfoDTO dto = new PersonInfoDTO();
        dto.setFirstName("John");
        dto.setLastName("Boyd");

        when(personInfoMapper.toPersonInfoDto(any(), any(), anyInt())).thenReturn(dto);

        List<PersonInfoDTO> result =
                personInfoService.getPersonsByLastName("Boyd");

        assertEquals(1, result.size());

        PersonInfoDTO returned = result.get(0);
        assertEquals("John", returned.getFirstName());
        assertEquals("Boyd", returned.getLastName());

        verify(personInfoMapper).toPersonInfoDto(any(), any(), anyInt());
    }

    @Test
    @DisplayName("Should set age to 0 when medical record is missing")
    void shouldSetAgeToZeroWhenMedicalRecordIsMissing() {

        Person person = new Person("John", "Boyd", "1509 Culver St");
        person.setCity("Culver");
        person.setEmail("john@email.com");

        addPersons(person);

        PersonInfoDTO dto = new PersonInfoDTO();
        dto.setFirstName("John");
        dto.setLastName("Boyd");

        when(personInfoMapper.toPersonInfoDto(any(), any(), anyInt())).thenReturn(dto);

        List<PersonInfoDTO> result =
                personInfoService.getPersonsByLastName("Boyd");

        assertEquals(1, result.size());

        verify(personInfoMapper).toPersonInfoDto(any(), isNull(), eq(0));
    }
}

