package com.openclassrooms.SafetyNetAlerts.Model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
// DTO data object transfer
public class SafetyNetData {

    private List<Person> persons;

    private List<Firestation> firestations;

    private List<MedicalRecord> medicalrecords;
}
