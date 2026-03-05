package com.openclassrooms.safetynetalerts.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * DTO représentant un enfant retourné par l'endpoint
 * {@code GET /childAlert}.
 *
 * <p>Contient les informations de l'enfant ainsi que
 * les adultes vivant dans le même foyer.</p>
 *
 * @since 1.0
 */
@Getter
@Setter
public class ChildAlertDTO {
    /**
     * Prénom de l'enfant.
     */
    private String firstName;
    /**
     * Nom de famille de l'enfant.
     */
    private String lastName;
    /**
     * Âge de l'enfant.
     */
    private int age;
    /**
     * Liste des adultes vivant dans le même foyer.
     */
    private List<AdultDTO> householdMembers;
}
