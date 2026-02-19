package com.openclassrooms.SafetyNetAlerts.Dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
//Représente une personne pour /firestation
public class FirestationPersonDTO {
    private String firstName;
    private String lastName;
    private String address;
    private String phone;

}
