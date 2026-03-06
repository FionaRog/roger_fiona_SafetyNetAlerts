package com.openclassrooms.safetynetalerts.controller.endpoint;

import com.openclassrooms.safetynetalerts.service.endpoint.IPhoneAlertService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Contrôleur REST exposant l'endpoint permettant de récupérer
 * les numéros de téléphone des personnes couvertes par une station.
 *
 * <p>Responsabilité :
 * recevoir la requête HTTP, déléguer la logique métier au service
 * {@link IPhoneAlertService}, puis retourner le résultat au format JSON.</p>
 *
 * <p>Endpoint exposé : {@code GET /phoneAlert?firestation=...}</p>
 *
 * <p>Code HTTP retourné : {@code 200 OK} (liste vide si aucun résultat).</p>
 *
 * @since 1.0
 */
@RestController
public class PhoneAlertController {

    private static final Logger logger = LoggerFactory.getLogger(PhoneAlertController.class);

    private final IPhoneAlertService phoneAlertService;

    /**
     * Construit le contrôleur PhoneAlert.
     *
     * @param phoneAlertService service métier associé
     * @since 1.0
     */
    public PhoneAlertController(IPhoneAlertService phoneAlertService) {
        this.phoneAlertService = phoneAlertService;
    }

    /**
     * Endpoint permettant de retourner les numéros de téléphone
     * correspondant à une station donnée.
     *
     * @param firestation numéro de station (paramètre obligatoire)
     * @return liste des numéros de téléphone (peut être vide)
     * @since 1.0
     */
    @GetMapping("/phoneAlert")
    public List<String> getPhoneNumber(@RequestParam("firestation") String firestation) {
        logger.info("Received request GET /phoneAlert?firestation={}", firestation);
        List<String> phoneByFirestation = phoneAlertService.getPhoneByFirestation(firestation);

        int count = phoneByFirestation == null ? 0 : phoneByFirestation.size();

        logger.info("PhoneAlert success: {} phone numbers returned for station '{}'", count, firestation);
        return phoneByFirestation;
    }

}
