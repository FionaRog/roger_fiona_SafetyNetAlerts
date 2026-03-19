package com.openclassrooms.safetynetalerts.controller.crud;

import com.openclassrooms.safetynetalerts.model.Firestation;
import com.openclassrooms.safetynetalerts.service.IFirestationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Contrôleur REST exposant les opérations CRUD
 * sur les mappings adresse ↔ station.
 *
 * <p>Endpoints exposés : </p>
 * <ul>
 *   <li>{@code GET /firestation} (sans paramètre stationNumber)</li>
 *   <li>{@code POST /firestation}</li>
 *   <li>{@code PUT /firestation}</li>
 *   <li>{@code DELETE /firestation?address=...}</li>
 *   <li>{@code DELETE /firestation?station=...}</li>
 * </ul>
 *
 *
 * <p>Responsabilité :
 * adapter les requêtes HTTP vers le service {@link IFirestationService}
 * et retourner les codes HTTP appropriés.</p>
 *
 * @since 1.0
 */
@RestController
public class FirestationCrudController {

    private static final Logger logger = LoggerFactory.getLogger(FirestationCrudController.class);

    private final IFirestationService firestationService;

    /**
     * Construit le contrôleur CRUD Firestation.
     *
     * @param firestationService service métier associé
     * @since 1.0
     */
    public FirestationCrudController(IFirestationService firestationService) {
        this.firestationService = firestationService;
    }

    /**
     * Retourne l'ensemble des mappings adresse ↔ station.
     *
     * <p>Code HTTP : {@code 200 OK}.</p>
     *
     * @return liste des {@link Firestation}
     * @since 1.0
     */
    @GetMapping(value = "/firestation", params = "!stationNumber")
    public List<Firestation> getMapping() {
        logger.info("Received request GET /firestation");
        return firestationService.getFirestation();
    }

    /**
     * Ajoute un nouveau mapping adresse ↔ station.
     *
     * <p>Codes HTTP :</p>
     * <ul>
     *   <li>{@code 201 Created} si le mapping est ajouté</li>
     *   <li>{@code 409 Conflict} si le mapping existe déjà
     *       ou si les données sont invalides</li>
     * </ul>
     *
     * @param firestation mapping à ajouter
     * @return réponse HTTP sans corps
     * @since 1.0
     */
    @PostMapping("/firestation")
    public ResponseEntity<Void> addMapping(@RequestBody Firestation firestation) {
        logger.info("POST /firestation - request received: {}", firestation);
        boolean added = firestationService.addFirestation(firestation);
        return added
                ? ResponseEntity.status(HttpStatus.CREATED).build()
                : ResponseEntity.status(HttpStatus.CONFLICT).build();
    }

    /**
     * Met à jour la station associée à une adresse.
     *
     * <p>Codes HTTP :</p>
     * <ul>
     *   <li>{@code 204 No Content} si la mise à jour réussit</li>
     *   <li>{@code 404 Not Found} si aucun mapping ne correspond à l'adresse</li>
     * </ul>
     *
     * @param firestation mapping contenant l'adresse cible et la nouvelle station
     * @return réponse HTTP sans corps
     * @since 1.0
     */
    @PutMapping("/firestation")
    public ResponseEntity<Void> updateMapping(@RequestBody Firestation firestation) {
        logger.info("PUT /firestation - request received: {}", firestation);
        boolean updated = firestationService.updateFirestationByAddress(firestation);
        return updated
                ? ResponseEntity.noContent().build()
                : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    /**
     * Supprime le mapping correspondant à l'adresse fournie.
     *
     * <p>Codes HTTP :</p>
     * <ul>
     *   <li>{@code 204 No Content} si suppression effectuée</li>
     *   <li>{@code 404 Not Found} si aucun mapping ne correspond</li>
     * </ul>
     *
     * @param address adresse à supprimer
     * @return réponse HTTP sans corps
     * @since 1.0
     */
    @DeleteMapping(value = "/firestation", params = "address")
    public ResponseEntity<Void> deleteByAddress(@RequestParam String address) {
        logger.info("DELETE /firestation - request received for address: {}", address);
        boolean deleted = firestationService.deleteFirestationByAddress(address);
        return deleted
                ? ResponseEntity.noContent().build()
                : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    /**
     * Supprime tous les mappings correspondant à la station fournie.
     *
     * <p>Codes HTTP :</p>
     * <ul>
     *   <li>{@code 204 No Content} si suppression effectuée</li>
     *   <li>{@code 404 Not Found} si aucun mapping ne correspond</li>
     * </ul>
     *
     * @param station station à supprimer
     * @return réponse HTTP sans corps
     * @since 1.0
     */
    @DeleteMapping(value = "/firestation", params = "station")
    public ResponseEntity<Void> deleteByStation(@RequestParam String station) {
        logger.info("DELETE /firestation - request received for station: {}", station);
        boolean deleted = firestationService.deleteFirestationByStation(station);
        return deleted
                ? ResponseEntity.noContent().build()
                : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}
