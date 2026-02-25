package com.openclassrooms.safetynetalerts.dto;

import com.openclassrooms.safetynetalerts.model.Firestation;
import com.openclassrooms.safetynetalerts.model.MedicalRecord;
import com.openclassrooms.safetynetalerts.model.Person;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SafetyNetDataDTO {

    private List<Person> persons;

    private List<Firestation> firestations;

    private List<MedicalRecord> medicalrecords;
}
