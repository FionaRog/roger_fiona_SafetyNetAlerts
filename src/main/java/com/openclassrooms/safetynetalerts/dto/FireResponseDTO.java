package com.openclassrooms.safetynetalerts.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class FireResponseDTO {

    private List<FirePersonDTO> firePersonDtos;
    private String station;

}
