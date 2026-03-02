package com.openclassrooms.safetynetalerts.controller.endpoint;

import com.openclassrooms.safetynetalerts.dto.FirestationResponseDTO;
import com.openclassrooms.safetynetalerts.service.IFirestationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class FirestationController {

    private static final Logger logger = LoggerFactory.getLogger(FirestationController.class);

    private final IFirestationService firestationService;

    public FirestationController(IFirestationService firestationService) {
        this.firestationService = firestationService;
    }


    @GetMapping(value = "/firestation", params = "stationNumber")
    public FirestationResponseDTO getPersonsByFirestation(@RequestParam("stationNumber") String stationNumber) {
        logger.info("Received request GET /firestation?stationNumber={}", stationNumber);
        FirestationResponseDTO personsByFirestation = firestationService.getPersonsByFirestation(stationNumber);

        logger.info("Firestation success : {} persons returned for station '{}', including {} children and {} adults",
                personsByFirestation.getPeople().size(), stationNumber, personsByFirestation.getChildren(), personsByFirestation.getAdults());
        return personsByFirestation;
    }

}