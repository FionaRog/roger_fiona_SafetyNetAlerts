package com.openclassrooms.safetynetalerts.controller.endpoint;

import com.openclassrooms.safetynetalerts.dto.ChildAlertDTO;
import com.openclassrooms.safetynetalerts.service.endpoint.IChildAlertService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
public class ChildAlertController {

    private  static final Logger logger = LoggerFactory.getLogger(ChildAlertController.class);

    private final IChildAlertService childAlertService;

    public ChildAlertController (IChildAlertService childAlertService) {
        this.childAlertService = childAlertService;
    }


    @GetMapping("/childAlert")
    public List<ChildAlertDTO> getChildAlertByAddress (@RequestParam String address) {
        logger.info("Received request GET /childAlert?address={}", address);
        List<ChildAlertDTO> childAlertByAddress = childAlertService.getChildByAddress(address);

        logger.info("childAlert success : '{}' children returned", childAlertByAddress.size());
        return childAlertByAddress;

    }
}
