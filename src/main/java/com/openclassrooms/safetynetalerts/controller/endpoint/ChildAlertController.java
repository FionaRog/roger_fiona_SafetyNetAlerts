package com.openclassrooms.safetynetalerts.controller.endpoint;

import com.openclassrooms.safetynetalerts.dto.ChildAlertDTO;
import com.openclassrooms.safetynetalerts.service.endpoint.IChildAlertService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Contrôleur REST exposant l'endpoint permettant de récupérer
 * les enfants résidant à une adresse donnée.
 *
 * <p>Responsabilité :
 * recevoir la requête HTTP, déléguer la logique métier au service
 * {@link IChildAlertService}, puis retourner le résultat au format JSON.</p>
 *
 * <p>Endpoint exposé : {@code GET /childAlert?address=...}</p>
 *
 * @since 1.0
 */
@RestController
public class ChildAlertController {

    private static final Logger logger = LoggerFactory.getLogger(ChildAlertController.class);

    private final IChildAlertService childAlertService;

    /**
     * Constructeur avec injection de dépendance du service métier.
     *
     * @param childAlertService service responsable de la récupération
     *                          des enfants par adresse
     */
    public ChildAlertController(IChildAlertService childAlertService) {
        this.childAlertService = childAlertService;
    }

    /**
     * Retourne la liste des enfants résidant à l'adresse fournie.
     *
     * <p>Code HTTP retourné : {@code 200 OK}.</p>
     * <p>Si aucune correspondance n'est trouvée, une liste vide est retournée.</p>
     *
     * @param address adresse recherchée (paramètre obligatoire)
     * @return liste des {@link ChildAlertDTO} correspondant à l'adresse
     */
    @GetMapping("/childAlert")
    public List<ChildAlertDTO> getChildAlertByAddress(@RequestParam("address") String address) {
        logger.info("Received request GET /childAlert?address={}", address);
        List<ChildAlertDTO> childAlertByAddress = childAlertService.getChildByAddress(address);

        logger.info("ChildAlert success : {} children returned for address '{}'", childAlertByAddress.size(), address);
        return childAlertByAddress;

    }
}
