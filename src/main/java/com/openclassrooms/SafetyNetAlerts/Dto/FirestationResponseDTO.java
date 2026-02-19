package com.openclassrooms.SafetyNetAlerts.Dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
//représente la réponse (FirestationPersonDTO + nombre d'adultes et d'enfants) pour /firestation
public class FirestationResponseDTO {
    private List<FirestationPersonDTO> people;
    private int adults;
    private int children;
}
