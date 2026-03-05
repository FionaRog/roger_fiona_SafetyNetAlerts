package com.openclassrooms.safetynetalerts.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * DTO représentant un adulte vivant dans le même foyer qu'un enfant
 * dans la réponse de l'endpoint {@code GET /childAlert}.
 *
 * @since 1.0
 */
@Getter
@Setter
public class AdultDTO {

    /**
     * Prénom de l'adulte.
     */
    private String firstName;
    /**
     * Nom de famille de l'adulte.
     */
    private String lastName;
}
