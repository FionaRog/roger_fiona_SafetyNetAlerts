package com.openclassrooms.safetynetalerts.service.crud;

import com.openclassrooms.safetynetalerts.model.MedicalRecord;
import com.openclassrooms.safetynetalerts.repository.DataLoader;
import com.openclassrooms.safetynetalerts.service.crud.MedicalRecordCrudService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class MedicalRecordCrudServiceTest {

    private MedicalRecordCrudService medicalRecordCrudService;

    @BeforeEach
    void setUp() {
        DataLoader.MEDICAL_RECORDS.clear();
        medicalRecordCrudService = new MedicalRecordCrudService();
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
    }
}
