package com.openclassrooms.safetynetalerts.service.crud;

import com.openclassrooms.safetynetalerts.model.MedicalRecord;
import com.openclassrooms.safetynetalerts.repository.DataLoader;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implémentation du service CRUD permettant de gérer les {@link MedicalRecord}.
 *
 * <p>Les données sont manipulées en mémoire via {@code DataLoader.DATASOURCE}.</p>
 *
 * <p>Règles principales :</p>
 * <ul>
 *   <li>un dossier médical est identifié par le couple prénom + nom (comparaison insensible à la casse),</li>
 *   <li>l'ajout est refusé si un doublon existe,</li>
 *   <li>la mise à jour remplace birthdate, medications et allergies,</li>
 *   <li>la suppression retire l'entrée correspondante si elle existe.</li>
 * </ul>
 *
 * @since 1.0
 */
@Service
public class MedicalRecordCrudService implements IMedicalRecordCrudService {

    private static final Logger logger = LoggerFactory.getLogger(MedicalRecordCrudService.class);

    /**
     * Retourne l'ensemble des dossiers médicaux.
     *
     * @return liste des {@link MedicalRecord} (copie de la source)
     * @since 1.0
     */
    public List<MedicalRecord> getMedicalRecord() {

        return new ArrayList<>(DataLoader.DATASOURCE.getMedicalrecords());
    }

    /**
     * Ajoute un nouveau dossier médical.
     *
     * <p>Refuse l'ajout si un dossier existe déjà avec le même prénom et nom
     * (comparaison insensible à la casse).</p>
     *
     * @param newMedicalRecord dossier médical à ajouter
     * @return {@code true} si ajouté, {@code false} sinon
     * @since 1.0
     */
    public boolean addMedicalRecord (MedicalRecord newMedicalRecord) {

        for(MedicalRecord medicalRecord : DataLoader.DATASOURCE.getMedicalrecords()) {
            if (medicalRecord.getFirstName().equalsIgnoreCase(newMedicalRecord.getFirstName())
                    && medicalRecord.getLastName().equalsIgnoreCase(newMedicalRecord.getLastName())) {
                logger.debug("add medicalRecord rejected: duplicate for {} {}",
                        newMedicalRecord.getFirstName(), newMedicalRecord.getLastName());
                return false;
            }
        }
        DataLoader.DATASOURCE.getMedicalrecords().add(newMedicalRecord);

        logger.debug("medicalRecord added for {} {}",
                newMedicalRecord.getFirstName(), newMedicalRecord.getLastName());
        return true;

    }

    /**
     * Met à jour un dossier médical existant identifié par prénom + nom.
     *
     * <p>Champs mis à jour :</p>
     * <ul>
     *   <li>date de naissance</li>
     *   <li>médicaments</li>
     *   <li>allergies</li>
     * </ul>
     *
     * @param updatedMedicalRecord dossier médical contenant les champs mis à jour
     * @return {@code true} si mise à jour effectuée, {@code false} sinon
     * @since 1.0
     */
    public boolean updateMedicalRecord (MedicalRecord updatedMedicalRecord) {

        for(MedicalRecord medicalRecord : DataLoader.DATASOURCE.getMedicalrecords()) {
            if(medicalRecord.getFirstName().equalsIgnoreCase(updatedMedicalRecord.getFirstName())
                    && medicalRecord.getLastName().equalsIgnoreCase(updatedMedicalRecord.getLastName())) {

                medicalRecord.setMedications(updatedMedicalRecord.getMedications());
                medicalRecord.setAllergies(updatedMedicalRecord.getAllergies());
                medicalRecord.setBirthdate(updatedMedicalRecord.getBirthdate());

                logger.debug("medicalRecord updated for {} {}",
                        medicalRecord.getFirstName(), medicalRecord.getLastName());
                return true;
            }
        }
        logger.debug("update medicalRecord rejected for {} {}",
                updatedMedicalRecord.getFirstName(), updatedMedicalRecord.getLastName());
        return false;
    }

    /**
     * Supprime un dossier médical à partir du prénom et du nom.
     *
     * <p>Si {@code firstName} ou {@code lastName} est null/blanc, la suppression est refusée.</p>
     *
     * @param firstName prénom de la personne
     * @param lastName nom de famille de la personne
     * @return {@code true} si suppression effectuée, {@code false} sinon
     * @since 1.0
     */
    public boolean deletedMedicalRecord (String firstName, String lastName) {

        if(firstName == null || firstName.isBlank() || lastName == null || lastName.isBlank()) {
            logger.debug("Delete medicalRecord rejected, firstName/lastName is null/blank");
            return false;
        }

        int before = DataLoader.DATASOURCE.getMedicalrecords().size();

        DataLoader.DATASOURCE.getMedicalrecords().removeIf(mr ->
                mr.getFirstName() != null
                && mr.getLastName() != null
                && mr.getFirstName().toLowerCase().equalsIgnoreCase(firstName)
                && mr.getLastName().toLowerCase().equalsIgnoreCase(lastName)
        );

        int after = DataLoader.DATASOURCE.getMedicalrecords().size();

        logger.debug("medicalRecord deleted for {} {}", firstName, lastName);
        return after < before;
    }
}
