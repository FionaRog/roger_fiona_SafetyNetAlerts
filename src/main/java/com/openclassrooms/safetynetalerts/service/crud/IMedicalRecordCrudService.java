package com.openclassrooms.safetynetalerts.service.crud;

import com.openclassrooms.safetynetalerts.model.MedicalRecord;

import java.util.List;

/**
 * Contrat du service CRUD permettant de gérer les {@link MedicalRecord}.
 *
 * <p>Ce service fournit les opérations de lecture, ajout, mise à jour et suppression
 * des dossiers médicaux dans la source de données en mémoire.</p>
 *
 * @since 1.0
 */
public interface IMedicalRecordCrudService {

    /**
     * Retourne l'ensemble des dossiers médicaux.
     *
     * @return liste des {@link MedicalRecord}
     * @since 1.0
     */
    List<MedicalRecord> getMedicalRecord();

    /**
     * Ajoute un nouveau dossier médical.
     *
     * <p>L'ajout est refusé si un dossier existe déjà avec le même prénom et nom
     * (comparaison après normalisation (trim + lowercase)).</p>
     *
     * @param newMedicalRecord dossier médical à ajouter
     * @return {@code true} si le dossier est ajouté, {@code false} sinon
     * @since 1.0
     */
    boolean addMedicalRecord(MedicalRecord newMedicalRecord);

    /**
     * Met à jour un dossier médical existant.
     *
     * <p>Le dossier à mettre à jour est identifié par prénom + nom
     * (comparaison après normalisation (trim + lowercase)).</p>
     *
     * @param updatedMedicalRecord dossier médical contenant les informations mises à jour
     * @return {@code true} si la mise à jour est effectuée, {@code false} sinon
     * @since 1.0
     */
    boolean updateMedicalRecord(MedicalRecord updatedMedicalRecord);

    /**
     * Supprime un dossier médical à partir du prénom et du nom.
     *
     * @param firstName prénom de la personne
     * @param lastName  nom de famille de la personne
     * @return {@code true} si la suppression est effectuée, {@code false} sinon
     * @since 1.0
     */
    boolean deleteMedicalRecord(String firstName, String lastName);


}
