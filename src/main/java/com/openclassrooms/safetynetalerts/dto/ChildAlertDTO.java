package com.openclassrooms.safetynetalerts.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ChildAlertDTO {
    private String firstName;
    private String lastName;
    private int age;
    private List<AdultDTO> householdMembers;

}
