package com.openclassrooms.safetynetalerts.controller.crud;

import com.openclassrooms.safetynetalerts.model.MedicalRecord;
import com.openclassrooms.safetynetalerts.service.crud.IMedicalRecordCrudService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Contrôleur REST exposant les opérations CRUD sur les dossiers médicaux.
 *
 * <p>Endpoints exposés :</p>
 * <ul>
 *   <li>{@code GET /medicalRecord}</li>
 *   <li>{@code POST /medicalRecord}</li>
 *   <li>{@code PUT /medicalRecord}</li>
 *   <li>{@code DELETE /medicalRecord?firstName=...&lastName=...}</li>
 * </ul>
 *
 * <p>Responsabilité :
 * adapter les requêtes HTTP vers le service {@link IMedicalRecordCrudService}
 * et retourner les codes HTTP appropriés.</p>
 *
 * @since 1.0
 */
@RestController
public class MedicalRecordCrudController {

    private final IMedicalRecordCrudService medicalRecordCrudService;

    /**
     * Construit le contrôleur CRUD MedicalRecord.
     *
     * @param medicalRecordCrudService service métier associé
     * @since 1.0
     */
    public MedicalRecordCrudController (IMedicalRecordCrudService medicalRecordCrudService) {
        this.medicalRecordCrudService = medicalRecordCrudService;
    }

    /**
     * Retourne l'ensemble des dossiers médicaux.
     *
     * <p>Code HTTP : {@code 200 OK}.</p>
     *
     * @return liste des {@link MedicalRecord}
     * @since 1.0
     */
    @GetMapping("/medicalRecord")
    public List<MedicalRecord>  getMedicalRecord() {

        return medicalRecordCrudService.getMedicalRecord();
    }

    /**
     * Ajoute un nouveau dossier médical.
     *
     * <p>Codes HTTP :</p>
     * <ul>
     *   <li>{@code 201 Created} si le dossier est ajouté</li>
     *   <li>{@code 409 Conflict} si le dossier existe déjà ou si les données sont invalides</li>
     * </ul>
     *
     * @param medicalRecord dossier médical à ajouter
     * @return réponse HTTP sans corps
     * @since 1.0
     */
    @PostMapping("/medicalRecord")
    public ResponseEntity<Void> addMedicalRecord (@RequestBody MedicalRecord medicalRecord) {

        boolean added = medicalRecordCrudService.addMedicalRecord(medicalRecord);
        return added
                ? ResponseEntity.status(HttpStatus.CREATED).build()
                : ResponseEntity.status(HttpStatus.CONFLICT).build();

    }

    /**
     * Met à jour un dossier médical existant.
     *
     * <p>Codes HTTP :</p>
     * <ul>
     *   <li>{@code 204 No Content} si la mise à jour réussit</li>
     *   <li>{@code 404 Not Found} si aucun dossier ne correspond</li>
     * </ul>
     *
     * @param medicalRecord dossier médical contenant les informations mises à jour
     * @return réponse HTTP sans corps
     * @since 1.0
     */
    @PutMapping("/medicalRecord")
    public ResponseEntity<Void> updateMedicalRecord (@RequestBody MedicalRecord medicalRecord) {

        boolean updated = medicalRecordCrudService.updateMedicalRecord(medicalRecord);
        return updated
                ? ResponseEntity.noContent().build()
                : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    /**
     * Supprime un dossier médical à partir du prénom et du nom.
     *
     * <p>Codes HTTP :</p>
     * <ul>
     *   <li>{@code 204 No Content} si suppression effectuée</li>
     *   <li>{@code 404 Not Found} si aucun dossier ne correspond</li>
     * </ul>
     *
     * @param firstName prénom de la personne
     * @param lastName nom de famille de la personne
     * @return réponse HTTP sans corps
     * @since 1.0
     */
    @DeleteMapping("/medicalRecord")
    public ResponseEntity<Void> deleteMedicalRecord (@RequestParam String firstName, @RequestParam String lastName) {

        boolean deleted = medicalRecordCrudService.deletedMedicalRecord(firstName, lastName);
        return deleted
                ? ResponseEntity.noContent().build()
                : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}
