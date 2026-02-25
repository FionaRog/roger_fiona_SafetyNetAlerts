package com.openclassrooms.safetynetalerts.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Setter
@Getter
public class MedicalRecord {

    private String firstName;

    private String lastName;

    private String birthdate;

    private List<String> medications;

    private List<String> allergies;
}
