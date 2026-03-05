package com.openclassrooms.safetynetalerts.model;

import lombok.Getter;
import lombok.Setter;

/**
 * Entité représentant une personne dans le système SafetyNetAlerts.
 *
 * <p>Contient les informations personnelles et de contact
 * utilisées par plusieurs endpoints de l'application.</p>
 *
 * <p>Ces données sont notamment utilisées pour construire
 * les différentes réponses API via les DTO.</p>
 *
 * @since 1.0
 */
@Getter
@Setter
public class Person {
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
     * Ville de résidence.
     */
    private String city;
    /**
     * Code postal de la résidence.
     */

    private String zip;
    /**
     * Numéro de téléphone.
     */
    private String phone;
    /**
     * Adresse email de la personne.
     */
    private String email;


}
