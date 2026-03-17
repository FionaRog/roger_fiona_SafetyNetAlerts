package com.openclassrooms.safetynetalerts.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Entité représentant le dossier médical d'une personne.
 *
 * <p>Un dossier médical contient le nom, prénom, la date de naissance,
 * les médicaments et les allergies d'une personne.</p>
 *
 * <p>Ces informations sont utilisées par plusieurs endpoints
 * pour calculer l'âge et exposer les données médicales.</p>
 *
 * @since 1.0
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MedicalRecord {

    /**
     * Construit un dossier médical minimal pour une personne donnée.
     *
     * <p>Ce constructeur est principalement utilisé dans les tests unitaires
     * lorsque seules les informations nécessaires au calcul de l'âge sont requises.</p>
     *
     * <p>Les listes de médicaments et d'allergies ne sont pas initialisées par ce constructeur
     * et peuvent être renseignées ultérieurement si nécessaire.</p>
     *
     * @param firstName prénom de la personne concernée
     * @param lastName  nom de famille
     * @param birthdate date de naissance au format attendu par l'application
     */
    public MedicalRecord(String firstName, String lastName, String birthdate) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthdate = birthdate;
    }

    /**
     * Prénom de la personne associée au dossier médical.
     */
    private String firstName;

    /**
     * Nom de famille de la personne associée au dossier médical.
     */
    private String lastName;

    /**
     * Date de naissance de la personne.
     *
     * <p>Utilisée pour calculer l'âge dans différents services.</p>
     */
    private String birthdate;

    /**
     * Liste des médicaments pris par la personne.
     */
    private List<String> medications = new ArrayList<>();

    /**
     * Liste des allergies de la personne.
     */
    private List<String> allergies = new ArrayList<>();
}
