package com.openclassrooms.safetynetalerts.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * DTO représentant un foyer couvert dans la réponse de l'endpoint
 * {@code GET /flood/stations}.
 *
 * <p>Un foyer correspond à une adresse et contient la liste
 * des résidents vivant à cette adresse.</p>
 *
 * @since 1.0
 */
@Getter
@Setter
public class FloodHouseholdDTO {

    /**
     * Adresse du foyer.
     */
    private String address;
    /**
     * Liste des résidents du foyer.
     */
    private List<FloodPersonDTO> residents = new ArrayList<>();
}
