package com.openclassrooms.safetynetalerts.controller.endpoint;

import com.openclassrooms.safetynetalerts.dto.FloodHouseholdDTO;
import com.openclassrooms.safetynetalerts.service.endpoint.IFloodService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Contrôleur REST exposant l'endpoint permettant de récupérer les foyers
 * couverts par une liste de stations.
 *
 * <p>Endpoint exposé : {@code GET /flood/stations?stations=1&stations=2...}</p>
 *
 * <p>Responsabilité :
 * recevoir la requête HTTP, déléguer la logique métier au service
 * {@link IFloodService}, puis retourner le résultat au format JSON.</p>
 *
 * <p>Code HTTP retourné : {@code 200 OK}. Si aucun résultat n'est trouvé,
 * une liste vide est retournée.</p>
 *
 * @since 1.0
 */
@RestController
public class FloodController {

    private static final Logger logger = LoggerFactory.getLogger(FloodController.class);

    private final IFloodService floodService;

    /**
     * Construit le contrôleur Flood.
     *
     * @param floodService service métier associé
     * @since 1.0
     */
    public FloodController(IFloodService floodService) {
        this.floodService = floodService;
    }

    /**
     * Endpoint permettant de retourner les foyers couverts par les stations fournies.
     *
     * @param stations liste des numéros de station (paramètre obligatoire)
     * @return liste de {@link FloodHouseholdDTO} (peut être vide)
     * @since 1.0
     */
    @GetMapping("/flood/stations")
    public List<FloodHouseholdDTO> getHouseholdsByStations(@RequestParam("stations") List<String> stations) {
        logger.info("Received request GET /flood/stations?stations={}", stations);
        List<FloodHouseholdDTO> householdsByStations = floodService.getHouseholdsByStations(stations);

        int count = householdsByStations == null ? 0 : householdsByStations.size();

        logger.info("Flood success: response built with {} household(s)", count);
        return householdsByStations;
    }
}
