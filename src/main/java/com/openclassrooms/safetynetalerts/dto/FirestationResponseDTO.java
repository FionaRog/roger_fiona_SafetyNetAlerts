package com.openclassrooms.safetynetalerts.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * DTO représentant la réponse de l'endpoint {@code GET /firestation?stationNumber=...}.
 *
 * <p>Contient la liste des personnes couvertes par une station ainsi que
 * le nombre d'adultes et d'enfants correspondants.</p>
 *
 * <p>Un enfant est défini comme ayant un âge {@code <= 18}.</p>
 *
 * @since 1.0
 */
@Getter
@Setter
public class FirestationResponseDTO {
    /**
     * Liste des personnes couvertes par la station.
     */
    private List<FirestationPersonDTO> people = new ArrayList<>();
    /**
     * Nombre total d'adultes parmi les personnes couvertes.
     */
    private int adults;
    /**
     * Nombre total d'enfants parmi les personnes couvertes.
     */
    private int children;
}
