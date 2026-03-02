package com.openclassrooms.safetynetalerts.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class FloodHouseholdDTO {

    private List<FloodPersonDTO> residents;
    String address;
}
