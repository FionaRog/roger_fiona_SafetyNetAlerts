package com.openclassrooms.safetynetalerts.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
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
@NoArgsConstructor
@AllArgsConstructor
public class Person {

    /**
     * Construit une personne avec ses informations minimales.
     *
     * <p>Ce constructeur est principalement utilisé dans les tests unitaires
     * lorsque seules les informations nécessaires à l'identification d'une
     * personne et à la localisation de son foyer sont requises.</p>
     *
     * <p>Les autres informations (ville, code postal, téléphone, e-mail)
     * peuvent être renseignées ultérieurement si nécessaire.</p>
     *
     * @param firstName prénom de la personne
     * @param lastName  nom de famille
     * @param address   adresse du domicile
     */
    public Person(String firstName, String lastName, String address) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
    }

    /**
     * Construit une personne avec ses informations minimales.
     *
     * <p>Ce constructeur est principalement utilisé dans les tests unitaires
     * lorsque seules les informations nécessaires à l'identification d'une
     * personne et à la localisation de son foyer sont requises.</p>
     *
     * <p>Les autres informations (ville, code postal, téléphone, e-mail)
     * peuvent être renseignées ultérieurement si nécessaire.</p>
     *
     * @param firstName prénom de la personne
     * @param lastName  nom de famille
     * @param address   adresse du domicile
     * @param phone     téléphone de la personne
     */
    public Person(String firstName, String lastName, String address, String phone) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.phone = phone;
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
