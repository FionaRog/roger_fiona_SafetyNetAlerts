package com.openclassrooms.SafetyNetAlerts.Model;

import lombok.Data;

import java.util.List;

@Data
// DTO data object transfer
public class SafetyNetData {

    private List<Person> persons;

    private List<FireStation> firestations;

    private List<MedicalRecord> medicalrecords;
}
