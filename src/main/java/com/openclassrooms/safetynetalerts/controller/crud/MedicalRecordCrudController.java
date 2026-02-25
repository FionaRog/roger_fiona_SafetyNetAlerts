package com.openclassrooms.safetynetalerts.controller.crud;

import com.openclassrooms.safetynetalerts.model.MedicalRecord;
import com.openclassrooms.safetynetalerts.service.crud.IMedicalRecordCrudService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class MedicalRecordCrudController {

    private final IMedicalRecordCrudService medicalRecordCrudService;

    public MedicalRecordCrudController (IMedicalRecordCrudService medicalRecordCrudService) {
        this.medicalRecordCrudService = medicalRecordCrudService;
    }

    @GetMapping("/medicalRecord")
    public List<MedicalRecord>  getMedicalRecord() {

        return medicalRecordCrudService.getMedicalRecord();
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
    public ResponseEntity<Void> deleteMedicalRecord (@RequestParam String firstName, @RequestParam String lastName) {

        boolean deleted = medicalRecordCrudService.deletedMedicalRecord(firstName, lastName);
        return deleted
                ? ResponseEntity.noContent().build()
                : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}
