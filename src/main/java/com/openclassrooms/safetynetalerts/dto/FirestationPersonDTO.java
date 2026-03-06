package com.openclassrooms.safetynetalerts.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * DTO représentant les informations d'une personne couverte par une station
 * retourné par l'endpoint {@code GET /firestation?stationNumber=...}.
 *
 * <p>Contient les informations de contact nécessaires
 * pour identifier et joindre la personne.</p>
 *
 * @since 1.0
 */
@Getter
@Setter
public class FirestationPersonDTO {

    /**
     * Prénom de la personne.
     */
    private String firstName;
    /**
     * Nom de famille de la personne.
     */
    private String lastName;
    /**
     * Adresse de résidence de la personne.
     */
    private String address;
    /**
     * Numéro de téléphone de la personne.
     */
    private String phone;
}
