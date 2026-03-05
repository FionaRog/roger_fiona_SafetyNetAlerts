package com.openclassrooms.safetynetalerts.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * DTO représentant la réponse de l'endpoint {@code GET /fire?address=...}.
 *
 * <p>Contient le numéro de station couvrant l'adresse
 * ainsi que la liste des personnes vivant à cette adresse.</p>
 *
 * @since 1.0
 */
@Getter
@Setter
public class FireResponseDTO {

    /**
     * Liste des personnes vivant à l'adresse fournie.
     */
    private List<FirePersonDTO> firePersonDtos;
    /**
     * Numéro de la station couvrant l'adresse.
     */
    private String station;
}
