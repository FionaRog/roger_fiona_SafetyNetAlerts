package com.openclassrooms.safetynetalerts.service.crud;

import com.openclassrooms.safetynetalerts.model.MedicalRecord;

import java.util.List;

public interface IMedicalRecordCrudService {

    List<MedicalRecord> getMedicalRecord();

   boolean addMedicalRecord (MedicalRecord newMedicalRecord);

    boolean updateMedicalRecord (MedicalRecord updatedMedicalRecord);

    boolean deletedMedicalRecord (String firstName, String lastName);


}
