package com.openclassrooms.safetynetalerts.dto;

import com.openclassrooms.safetynetalerts.model.Firestation;
import com.openclassrooms.safetynetalerts.model.MedicalRecord;
import com.openclassrooms.safetynetalerts.model.Person;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * DTO représentant l'ensemble des données chargées
 * par l'application SafetyNetAlerts.
 *
 * <p>Utilisé pour stocker les données issues du fichier JSON
 * chargé par {@code DataLoader}.</p>
 *
 * @since 1.0
 */
@Getter
@Setter
public class SafetyNetDataDTO {
    /**
     * Liste des personnes enregistrées.
     */
    private List<Person> persons;
    /**
     * Liste des mappings adresse ↔ station.
     */
    private List<Firestation> firestations;
    /**
     * Liste des dossiers médicaux.
     */
    private List<MedicalRecord> medicalrecords;
}
