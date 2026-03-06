package com.openclassrooms.safetynetalerts.controller.endpoint;

import com.openclassrooms.safetynetalerts.dto.FirestationResponseDTO;
import com.openclassrooms.safetynetalerts.service.IFirestationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Contrôleur REST exposant les endpoints liés aux casernes (firestations).
 *
 * <p>Endpoint exposé ici : {@code GET /firestation?stationNumber=...}</p>
 *
 * <p>Responsabilité :
 * recevoir la requête HTTP, déléguer la logique métier au service
 * {@link IFirestationService}, puis retourner le résultat au format JSON.</p>
 *
 * <p>Code HTTP retourné : {@code 200 OK}. En l'absence de résultat, un
 * {@link FirestationResponseDTO} "vide" est retourné (selon le service).</p>
 *
 * @since 1.0
 */
@RestController
public class FirestationController {

    private static final Logger logger = LoggerFactory.getLogger(FirestationController.class);

    private final IFirestationService firestationService;

    /**
     * Construit le contrôleur Firestation.
     *
     * @param firestationService service métier associé
     * @since 1.0
     */
    public FirestationController(IFirestationService firestationService) {
        this.firestationService = firestationService;
    }

    /**
     * Retourne les personnes couvertes par une station ainsi que le nombre d'adultes et d'enfants.
     *
     * <p>Un enfant est défini comme ayant un âge {@code <= 18}.</p>
     *
     * @param stationNumber numéro de station (paramètre obligatoire)
     * @return {@link FirestationResponseDTO} contenant la liste des personnes, le nombre d'adultes et d'enfants
     * @since 1.0
     */
    @GetMapping(value = "/firestation", params = "stationNumber")
    public FirestationResponseDTO getPersonsByFirestation(@RequestParam("stationNumber") String stationNumber) {
        logger.info("Received request GET /firestation?stationNumber={}", stationNumber);
        FirestationResponseDTO personsByFirestation = firestationService.getPersonsByFirestation(stationNumber);

        int peopleCount = personsByFirestation.getPeople() == null
                ? 0
                : personsByFirestation.getPeople().size();

        logger.info("Firestation success : {} persons returned for station '{}', including {} (children={}, adults={})",
                peopleCount, stationNumber, personsByFirestation.getChildren(), personsByFirestation.getAdults());
        return personsByFirestation;
    }

}