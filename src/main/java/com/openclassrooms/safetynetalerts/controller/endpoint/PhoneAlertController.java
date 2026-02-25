package com.openclassrooms.safetynetalerts.controller.endpoint;

import com.openclassrooms.safetynetalerts.service.endpoint.IPhoneAlertService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@RestController
public class PhoneAlertController {

    private static final Logger logger = LoggerFactory.getLogger(PhoneAlertController.class);

    private final IPhoneAlertService phoneAlertService;

    public PhoneAlertController (IPhoneAlertService phoneAlertService) {
        this.phoneAlertService = phoneAlertService;
    }


    @GetMapping("/phoneAlert")
    public List<String> getPhoneNumber(@RequestParam String firestation) {
        logger.info("Received request GET /phoneAlert?firestation={}",firestation);
        List<String> phoneByFirestation = phoneAlertService.getPhoneByFirestation(firestation);

        logger.info("phoneAlert success: {} phone numbers returned",phoneByFirestation.size());
        return phoneByFirestation;
    }

}
