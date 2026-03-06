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
