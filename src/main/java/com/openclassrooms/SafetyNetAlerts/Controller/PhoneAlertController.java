package com.openclassrooms.SafetyNetAlerts.Controller;

import com.openclassrooms.SafetyNetAlerts.Service.PhoneAlertService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@RestController
public class PhoneAlertController {

    private final PhoneAlertService phoneAlertService;

    public PhoneAlertController (PhoneAlertService phoneAlertService) {
        this.phoneAlertService = phoneAlertService;
    }

    @GetMapping("/phoneAlert")
    public List<String> getPhoneNumber(@RequestParam String firestation) {
        logger.info("phoneAlert called with firestation={}",firestation);
        List<String> phoneByFirestation = phoneAlertService.getPhoneByFirestation(firestation);

        logger.info("phoneAlert success: {} phone numbers returned",phoneByFirestation.size());
        return phoneByFirestation;
    }

    private static final Logger logger =
        LoggerFactory.getLogger(PhoneAlertController.class);

}
