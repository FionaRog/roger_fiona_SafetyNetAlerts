package com.openclassrooms.safetynetalerts.controller.endpoint;

import com.openclassrooms.safetynetalerts.service.endpoint.ICommunityEmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Contrôleur REST exposant l'endpoint permettant de récupérer
 * les adresses e-mail des habitants d'une ville donnée.
 *
 * <p>Endpoint exposé : {@code GET /communityEmail?city=...}</p>
 *
 * <p>Responsabilité :
 * recevoir la requête HTTP, déléguer la logique métier au service
 * {@link ICommunityEmailService}, puis retourner le résultat au format JSON.</p>
 *
 * <p>Code HTTP retourné : {@code 200 OK} (liste vide si aucun résultat).</p>
 *
 * @since 1.0
 */
@RestController
public class CommunityEmailController {

    private static final Logger logger = LoggerFactory.getLogger(CommunityEmailController.class);

    private final ICommunityEmailService communityEmailService;

    /**
     * Construit le contrôleur Community Email.
     *
     * @param communityEmailService service métier associé
     * @since 1.0
     */
    public CommunityEmailController(ICommunityEmailService communityEmailService) {
        this.communityEmailService = communityEmailService;
    }

    /**
     * Endpoint permettant de récupérer les adresses e-mail
     * correspondant à une ville donnée.
     *
     * @param city ville recherchée (paramètre obligatoire)
     * @return liste des e-mails correspondants
     * @since 1.0
     */
    @GetMapping("/communityEmail")
    public List<String> getCommunityEmails(@RequestParam("city") String city) {
        logger.info("Received request GET /communityEmail?city={}", city);
        List<String> emails = communityEmailService.getEmailsByCity(city);

        logger.info("communityEmail success: {} emails returned for city '{}'", emails.size(), city);
        return emails;
    }

}
