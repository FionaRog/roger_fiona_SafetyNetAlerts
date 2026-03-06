package com.openclassrooms.safetynetalerts.dto;

import com.openclassrooms.safetynetalerts.model.MedicalRecord;
import com.openclassrooms.safetynetalerts.model.Person;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * DTO représentant les informations détaillées d'une personne
 * retournées par l'endpoint {@code GET /personInfo}.
 *
 * <p>Construit à partir des données {@link Person} et
 * {@link MedicalRecord} correspondantes.</p>
 *
 * @since 1.0
 */
@Getter
@Setter
public class PersonInfoDTO {

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
     * Adresse e-mail de la personne.
     */
    private String email;
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
