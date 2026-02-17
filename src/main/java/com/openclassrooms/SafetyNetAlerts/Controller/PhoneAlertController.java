package com.openclassrooms.SafetyNetAlerts.Controller;

import com.openclassrooms.SafetyNetAlerts.Service.PhoneAlertService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class PhoneAlertController {

    private final PhoneAlertService phoneAlertService;

    public PhoneAlertController (PhoneAlertService phoneAlertService) {
        this.phoneAlertService = phoneAlertService;
    }

    @GetMapping("/phoneAlert")
    public List<String> getPhoneNumber(@RequestParam String firestation) {
        return phoneAlertService.getPhoneByFirestation(firestation);
    }




}
