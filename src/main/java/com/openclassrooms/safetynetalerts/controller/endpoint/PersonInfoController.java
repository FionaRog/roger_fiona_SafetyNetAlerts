package com.openclassrooms.safetynetalerts.controller.endpoint;

import com.openclassrooms.safetynetalerts.dto.PersonInfoDTO;
import com.openclassrooms.safetynetalerts.service.endpoint.IPersonInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Contrôleur REST exposant l'endpoint permettant de récupérer les informations
 * détaillées des personnes correspondant à un nom de famille.
 *
 * <p>Responsabilité :
 * recevoir la requête HTTP, déléguer la logique métier au service
 * {@link IPersonInfoService}, puis retourner le résultat au format JSON.</p>
 *
 * <p>Endpoint exposé : {@code GET /personInfo?lastName=...}</p>
 *
 * <p>Code HTTP retourné : {@code 200 OK} (liste vide si aucun résultat).</p>
 *
 * @since 1.0
 */
@RestController
public class PersonInfoController {

    private static final Logger logger = LoggerFactory.getLogger(PersonInfoController.class);

    private final IPersonInfoService personInfoService;

    /**
     * Construit le contrôleur PersonInfo.
     *
     * @param personInfoService service métier associé
     * @since 1.0
     */
    public PersonInfoController(IPersonInfoService personInfoService) {
        this.personInfoService = personInfoService;
    }

    /**
     * Endpoint permettant de retourner les informations des personnes correspondant au nom fourni.
     *
     * @param lastName nom de famille recherché (paramètre obligatoire)
     * @return liste de {@link PersonInfoDTO} (peut être vide)
     * @since 1.0
     */
    @GetMapping("/personInfo")
    public List<PersonInfoDTO> getPersonInfoByLastName(@RequestParam("lastName") String lastName) {
        logger.info("Received request GET /personInfo?lastName={}", lastName);
        List<PersonInfoDTO> personInfoByLastName = personInfoService.getPersonsByLastName(lastName);

        int count = personInfoByLastName == null ? 0 : personInfoByLastName.size();

        logger.info("PersonInfo success : {} persons returned with lastName '{}'", count, lastName);
        return personInfoByLastName;

    }
}
