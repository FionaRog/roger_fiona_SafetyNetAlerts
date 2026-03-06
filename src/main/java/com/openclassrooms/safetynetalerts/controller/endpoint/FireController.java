package com.openclassrooms.safetynetalerts.controller.endpoint;

import com.openclassrooms.safetynetalerts.dto.FireResponseDTO;
import com.openclassrooms.safetynetalerts.service.endpoint.IFireService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Contrôleur REST exposant l'endpoint permettant de récupérer les informations
 * relatives aux personnes vivant à une adresse donnée, ainsi que la caserne associée.
 *
 * <p>Endpoint exposé : {@code GET /fire?address=...}</p>
 *
 * <p>Responsabilité :
 * recevoir la requête HTTP, déléguer la logique métier au service
 * {@link IFireService}, puis retourner le résultat au format JSON.</p>
 *
 * <p>Code HTTP retourné : {@code 200 OK}. Si aucune donnée n'est trouvée,
 * un {@link FireResponseDTO} "vide" est retourné (selon le service).</p>
 *
 * @since 1.0
 */
@RestController
public class FireController {

    private static final Logger logger = LoggerFactory.getLogger(FireController.class);
    private final IFireService fireService;

    /**
     * Construit le contrôleur Fire.
     *
     * @param fireService service métier associé
     * @since 1.0
     */
    public FireController(IFireService fireService) {
        this.fireService = fireService;
    }

    /**
     * Endpoint permettant de récupérer la station associée à l'adresse
     * et la liste des personnes vivant à cette adresse.
     *
     * @param address adresse recherchée (paramètre obligatoire)
     * @return {@link FireResponseDTO} contenant le numéro de station et la liste des personnes
     * @since 1.0
     */
    @GetMapping("/fire")
    public FireResponseDTO getPersonsByAddress(@RequestParam("address") String address) {
        logger.info("Received request GET /fire?address={}", address);
        FireResponseDTO personsByAddress = fireService.getPersonByAddress(address);

        int count = personsByAddress.getFirePersonDtos() == null
                ? 0
                : personsByAddress.getFirePersonDtos().size();

        logger.info("Fire success : {} persons returned for address '{}', at station {}",
                count, address, personsByAddress.getStation());
        return personsByAddress;

    }
}
