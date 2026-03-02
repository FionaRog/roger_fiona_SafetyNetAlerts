package com.openclassrooms.safetynetalerts.controller.endpoint;

import com.openclassrooms.safetynetalerts.dto.FireResponseDTO;
import com.openclassrooms.safetynetalerts.service.endpoint.IFireService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class FireController {

    private static final Logger logger = LoggerFactory.getLogger(FireController.class);
    private final IFireService fireService;

    public FireController(IFireService fireService) {
        this.fireService = fireService;
    }

    @GetMapping("/fire")
    public FireResponseDTO getPersonsByAddress(@RequestParam ("address") String address) {
        logger.info("Received request GET /fire?address={}", address);
        FireResponseDTO personsByAddress = fireService.getPersonByAddress(address);

        logger.info("Fire success : {} persons returned for address '{}', at station {}",
                personsByAddress.getFirePersonDtos().size(), address, personsByAddress.getStation());
        return personsByAddress;

    }
}
