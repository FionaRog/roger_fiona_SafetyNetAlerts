package com.openclassrooms.SafetyNetAlerts.Controller;

import com.openclassrooms.SafetyNetAlerts.Service.CommunityEmailService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

//@restcontroller indique que la classe réponds en json
@RestController
public class CommunityEmailController {

    private final CommunityEmailService communityEmailService;

    //Injection du service: Spring fournit auto. l'instance
    public CommunityEmailController(CommunityEmailService communityEmailService) {
        this.communityEmailService = communityEmailService;
    }

    //Mappe l'url "/communityEmail : lie la méthode à l'url
    @GetMapping("/communityEmail")
    //@requestparam récupère le ?city=... dans l'url
    public List<String> getCommunityEmails(@RequestParam String city) {
        //Délègue la logique au service
        return communityEmailService.getEmailsByCity(city);
    }
}
