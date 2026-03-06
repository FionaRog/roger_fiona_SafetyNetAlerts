package com.openclassrooms.safetynetalerts.model;

import lombok.Getter;
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
public class MedicalRecord {

    /**
     * Construit un dossier médical pour une personne donnée.
     *
     * <p>Ce constructeur facilite l'initialisation d'un {@link MedicalRecord}
     * lors de tests unitaires ou de créations programmatiques.</p>
     *
     * @param firstName prénom de la personne concernée
     * @param lastName nom de famille
     * @param birthdate date de naissance au format attendu par l'application
     * @param medications liste des médicaments
     * @param allergies liste des allergies
     */
    public MedicalRecord(String firstName, String lastName, String birthdate,
                         List<String> medications, List<String> allergies) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthdate = birthdate;
        this.medications = medications;
        this.allergies = allergies;
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
