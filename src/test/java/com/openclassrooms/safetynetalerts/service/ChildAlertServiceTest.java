package com.openclassrooms.safetynetalerts.service;


import com.openclassrooms.safetynetalerts.dto.AdultDTO;
import com.openclassrooms.safetynetalerts.dto.ChildAlertDTO;
import com.openclassrooms.safetynetalerts.dto.SafetyNetDataDTO;
import com.openclassrooms.safetynetalerts.mapper.PersonMapper;
import com.openclassrooms.safetynetalerts.model.MedicalRecord;
import com.openclassrooms.safetynetalerts.model.Person;
import com.openclassrooms.safetynetalerts.repository.DataLoader;
import com.openclassrooms.safetynetalerts.service.endpoint.ChildAlertService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ChildAlertServiceTest {

    @Mock
    private PersonMapper personMapper;

    @InjectMocks
    private ChildAlertService childAlertService;

    @BeforeEach
    void setUp() {
        DataLoader.PERSONS.clear();
        DataLoader.MEDICAL_RECORDS.clear();
        DataLoader.FIRESTATIONS.clear();    }

    private void addPersons(Person... persons) {
        DataLoader.PERSONS.addAll(List.of(persons));
    }

    private void addMedicalRecords(MedicalRecord... records) {
        DataLoader.MEDICAL_RECORDS.addAll(List.of(records));
    }


    @Test
    @DisplayName("Should return empty list when address is null")
    void shouldReturnEmptyListWhenAddressIsNull() {
        List<ChildAlertDTO> result = childAlertService.getChildByAddress(null);

        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Should return empty list when address is blank")
    void shouldReturnEmptyListWhenAddressIsBlank() {
        List<ChildAlertDTO> result = childAlertService.getChildByAddress(" ");

        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Should return empty list when no household matches address")
    void shouldReturnEmptyListWhenNoHouseholdMatchesAddress() {
        Person person = new Person("John", "Boyd", "1509 culver St ");

        addPersons(person);

        List<ChildAlertDTO> result = childAlertService.getChildByAddress("Another address");

        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Should return empty list when household contains no children")
    void shouldReturnEmptyListWhenHouseholdContainsNoChildren() {
        Person person = new Person("John", "Boyd", "1509 culver St ");

        addPersons(person);

        MedicalRecord record = new MedicalRecord("John", "Boyd", "01/01/1980");

        addMedicalRecords(record);

        List<ChildAlertDTO> result = childAlertService.getChildByAddress("1509 culver St ");

        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Should return children with adult household members")
    void shouldReturnChildrenWithAdultHouseholdMembers() {
        Person adultPerson = new Person("John", "Boyd", "1509 culver St ");

        Person childPerson = new Person("Juliette", "Boyd", "1509 culver St ");

        addPersons(adultPerson, childPerson);

        MedicalRecord adultRecord = new MedicalRecord("John", "Boyd", "01/01/1980");

        MedicalRecord childRecord = new MedicalRecord("Juliette", "Boyd", "01/01/2008");

        addMedicalRecords(adultRecord, childRecord);

        AdultDTO adultDto = new AdultDTO();
        adultDto.setFirstName("John");
        adultDto.setLastName("Boyd");

        ChildAlertDTO childDto = new ChildAlertDTO();
        childDto.setFirstName("Juliette");
        childDto.setLastName("Boyd");

        when(personMapper.toChildAlertDto(childPerson)).thenReturn(childDto);
        when(personMapper.toAdultDto(adultPerson)).thenReturn(adultDto);

        List<ChildAlertDTO> result = childAlertService.getChildByAddress("1509 Culver St");

        assertEquals(1, result.size());

        ChildAlertDTO returnedChild = result.get(0);
        assertEquals("Juliette", returnedChild.getFirstName());
        assertEquals("Boyd", returnedChild.getLastName());
        assertTrue(returnedChild.getAge() <= 18);

        assertEquals(1, returnedChild.getHouseholdMembers().size());

        AdultDTO returnedAdult = returnedChild.getHouseholdMembers().get(0);
        assertEquals("John", returnedAdult.getFirstName());
        assertEquals("Boyd", returnedAdult.getLastName());

        verify(personMapper).toChildAlertDto(childPerson);
        verify(personMapper).toAdultDto(adultPerson);
    }

    @Test
    @DisplayName("Should ignore person when medical record is missing")
    void shouldIgnorePersonWhenMedicalRecordIsMissing() {

        Person childPerson = new Person("Juliette", "Boyd", "1509 culver St ");

        addPersons(childPerson);

        List<ChildAlertDTO> result = childAlertService.getChildByAddress("1509 Culver St");

        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Should ignore person when birthdate is null")
    void shouldIgnorePersonWhenBirthdateIsNull() {

        Person childPerson = new Person("Juliette", "Boyd", "1509 culver St ");

        addPersons(childPerson);

        MedicalRecord record = new MedicalRecord("Juliette", "Boyd", null);

        addMedicalRecords(record);

        List<ChildAlertDTO> result = childAlertService.getChildByAddress("1509 Culver St");

        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Should ignore person when birthdate is blank")
    void shouldIgnorePersonWhenBirthdateIsBlank() {

        Person childPerson = new Person("Juliette", "Boyd", "1509 culver St ");

        addPersons(childPerson);

        MedicalRecord record =
                new MedicalRecord("Juliette", "Boyd", "   ");

        addMedicalRecords(record);

        List<ChildAlertDTO> result =
                childAlertService.getChildByAddress("1509 Culver St");

        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Should ignore person when birthdate is invalid")
    void shouldIgnorePersonWhenBirthdateIsInvalid() {

        Person childPerson = new Person("Juliette", "Boyd", "1509 culver St ");

        addPersons(childPerson);

        MedicalRecord record =
                new MedicalRecord("Juliette", "Boyd", "invalid-date");

        addMedicalRecords(record);

        List<ChildAlertDTO> result =
                childAlertService.getChildByAddress("1509 Culver St");

        assertTrue(result.isEmpty());
    }
}

