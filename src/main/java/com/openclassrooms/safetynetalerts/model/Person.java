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
     * Construit une personne avec l'ensemble de ses informations.
     *
     * <p>Ce constructeur est principalement utilisé dans les tests ou lors
     * de la création programmatique d'objets {@link Person}.</p>
     *
     * @param firstName prénom de la personne
     * @param lastName nom de famille
     * @param address adresse du domicile
     * @param city ville de résidence
     * @param zip code postal
     * @param phone numéro de téléphone
     * @param email adresse e-mail
     */
    public Person(String firstName, String lastName, String address,
                  String city, String zip, String phone, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.city = city;
        this.zip = zip;
        this.phone = phone;
        this.email = email;
    }

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
