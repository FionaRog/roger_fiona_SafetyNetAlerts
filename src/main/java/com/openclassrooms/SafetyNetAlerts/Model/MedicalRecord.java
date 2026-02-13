package com.openclassrooms.SafetyNetAlerts.Model;

import lombok.Data;

// mettre getter et setter
@Data
public class MedicalRecord {

    private String firstName;

    private String lastName;

    private String birthdate;

    private String medications;

    private String allergies;
}
