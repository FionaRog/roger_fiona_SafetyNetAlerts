package com.openclassrooms.safetynetalerts.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * DTO représentant les informations détaillées d'une personne
 * retournées par l'endpoint {@code GET /fire?address=...}.
 *
 * <p>Inclut les informations personnelles ainsi que les données
 * médicales si disponibles.</p>
 *
 * @since 1.0
 */
@Getter
@Setter
public class FirePersonDTO {

    /**
     * Prénom de la personne.
     */
    private String firstName;
    /**
     * Nom de famille de la personne.
     */
    private String lastName;
    /**
     * Numéro de téléphone de la personne.
     */
    private String phone;
    /**
     * Âge de la personne (calculé à partir du dossier médical).
     */
    private int age;
    /**
     * Liste des médicaments associés à la personne.
     */
    private List<String> medications = new ArrayList<>();
    /**
     * Liste des allergies associées à la personne.
     */
    private List<String> allergies = new ArrayList<>();
}
