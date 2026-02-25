package com.openclassrooms.safetynetalerts.controller.endpoint;

import com.openclassrooms.safetynetalerts.service.endpoint.ICommunityEmailService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;


@RestController
public class CommunityEmailController {

    private static final Logger logger = LoggerFactory.getLogger(CommunityEmailController.class);

    private final ICommunityEmailService communityEmailService;

    public CommunityEmailController(ICommunityEmailService communityEmailService) {
        this.communityEmailService = communityEmailService;
    }


    @GetMapping("/communityEmail")
    public List<String> getCommunityEmails(@RequestParam String city) {
        logger.info("Received request GET /communityEmail?city={}", city);
        List<String> emails = communityEmailService.getEmailsByCity(city);

        logger.info("communityEmail success: {} emails returned", emails.size());
        return emails;
    }

}
