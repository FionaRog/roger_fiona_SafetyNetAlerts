package com.openclassrooms.safetynetalerts.controller.endpoint;

import com.openclassrooms.safetynetalerts.dto.FloodHouseholdDTO;
import com.openclassrooms.safetynetalerts.service.endpoint.IFloodService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class FloodController {

    private static final Logger logger = LoggerFactory.getLogger(FloodController.class);

    private final IFloodService floodService;

    public FloodController (IFloodService floodService) {
        this.floodService = floodService;
    }

    @GetMapping("/flood/stations")
    public List<FloodHouseholdDTO> getHouseholdsByStations (@RequestParam ("stations") List<String> stations) {
        logger.info("Received request GET /flood/stations?stations={}", stations);
        List<FloodHouseholdDTO> householdsByStations = floodService.getHouseholdsByStations(stations);

        logger.info("Flood success: response built with {} household(s)", householdsByStations.size());
        return householdsByStations;
    }
}
