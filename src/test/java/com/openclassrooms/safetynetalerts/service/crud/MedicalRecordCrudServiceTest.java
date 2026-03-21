package com.openclassrooms.safetynetalerts.service.crud;

import com.openclassrooms.safetynetalerts.model.MedicalRecord;
import com.openclassrooms.safetynetalerts.repository.DataLoader;
import com.openclassrooms.safetynetalerts.repository.DataPersistenceService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

public class MedicalRecordCrudServiceTest {

    private MedicalRecordCrudService medicalRecordCrudService;
    private DataPersistenceService dataPersistenceService;

    @BeforeEach
    void setUp() {
        DataLoader.MEDICAL_RECORDS.clear();
        dataPersistenceService = Mockito.mock(DataPersistenceService.class);
        medicalRecordCrudService = new MedicalRecordCrudService(dataPersistenceService);
    }

    private void addMedicalRecords(MedicalRecord... records) {
        DataLoader.MEDICAL_RECORDS.addAll(List.of(records));
    }

    @Test
    @DisplayName("Should add medical record when no duplicate exists")
    void shouldAddMedicalRecordWhenNoDuplicateExists() {

        MedicalRecord record =
                new MedicalRecord("John", "Boyd", "01/01/1980");

        boolean result = medicalRecordCrudService.addMedicalRecord(record);

        assertTrue(result);
        assertEquals(1, DataLoader.MEDICAL_RECORDS.size());

        verify(dataPersistenceService).saveData();
    }

    @Test
    @DisplayName("Should reject medical record when duplicate exists")
    void shouldRejectMedicalRecordWhenDuplicateExists() {

        addMedicalRecords(
                new MedicalRecord("John", "Boyd", "01/01/1980")
        );

        MedicalRecord duplicate =
                new MedicalRecord("John", "Boyd", "01/01/1981");

        boolean result = medicalRecordCrudService.addMedicalRecord(duplicate);

        assertFalse(result);
        assertEquals(1, DataLoader.MEDICAL_RECORDS.size());

        verify(dataPersistenceService, never()).saveData();
    }

    @Test
    @DisplayName("Should update medical record when matching person exists")
    void shouldUpdateMedicalRecordWhenMatchingPersonExists() {

        addMedicalRecords(
                new MedicalRecord("John", "Boyd", "01/01/1980")
        );

        MedicalRecord updated =
                new MedicalRecord("John", "Boyd", "01/01/1981");

        boolean result = medicalRecordCrudService.updateMedicalRecord(updated);

        assertTrue(result);
        assertEquals("01/01/1981",
                DataLoader.MEDICAL_RECORDS.get(0).getBirthdate());

        verify(dataPersistenceService).saveData();
    }

    @Test
    @DisplayName("Should delete medical record when matching person exists")
    void shouldDeleteMedicalRecordWhenMatchingPersonExists() {

        addMedicalRecords(
                new MedicalRecord("John", "Boyd", "01/01/1980")
        );

        boolean result =
                medicalRecordCrudService.deleteMedicalRecord("John", "Boyd");

        assertTrue(result);
        assertTrue(DataLoader.MEDICAL_RECORDS.isEmpty());

        verify(dataPersistenceService).saveData();
    }
}
