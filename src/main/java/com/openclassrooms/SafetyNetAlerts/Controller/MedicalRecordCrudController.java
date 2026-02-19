package com.openclassrooms.SafetyNetAlerts.Controller;

import com.openclassrooms.SafetyNetAlerts.Model.MedicalRecord;
import com.openclassrooms.SafetyNetAlerts.Service.MedicalRecordCrudService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class MedicalRecordCrudController {

    private final MedicalRecordCrudService medicalRecordCrudService;

    public MedicalRecordCrudController (MedicalRecordCrudService medicalRecordCrudService) {
        this.medicalRecordCrudService = medicalRecordCrudService;
    }

    @PostMapping("/medicalRecord")
    public ResponseEntity<Void> addMedicalRecord (@RequestBody MedicalRecord medicalRecord) {
        boolean added = medicalRecordCrudService.addMedicalRecord(medicalRecord);
        return added
                ? ResponseEntity.status(HttpStatus.CREATED).build()
                : ResponseEntity.status(HttpStatus.CONFLICT).build();

    }

    @PutMapping("/medicalRecord")
    public ResponseEntity<Void> updateMedicalRecord (@RequestBody MedicalRecord medicalRecord) {
        boolean updated = medicalRecordCrudService.updateMedicalRecord(medicalRecord);
        return updated
                ? ResponseEntity.noContent().build()
                : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @DeleteMapping("/medicalRecord")
    public ResponseEntity<Void> deleteMedicalRecord (@RequestParam String firstName,
                                                     @RequestParam String lastName) {
        boolean deleted = medicalRecordCrudService.deletedMedicalRecord(firstName, lastName);
        return deleted
                ? ResponseEntity.noContent().build()
                : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}
