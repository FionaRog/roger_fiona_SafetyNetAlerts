package com.openclassrooms.safetynetalerts.controller.endpoint;

import com.openclassrooms.safetynetalerts.dto.PersonInfoDTO;
import com.openclassrooms.safetynetalerts.service.endpoint.IPersonInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class PersonInfoController {

    private static final Logger logger = LoggerFactory.getLogger(PersonInfoController.class);

    private final IPersonInfoService personInfoService;

    public PersonInfoController(IPersonInfoService personInfoService) {
        this.personInfoService = personInfoService;
    }

    @GetMapping("/personInfo")
    public List<PersonInfoDTO> getPersonInfoByLastName (@RequestParam("lastName") String lastName) {
        logger.info("Received request GET /personInfo?lastName={}", lastName);
        List<PersonInfoDTO> personInfoByLastName = personInfoService.getPersonsByLastName(lastName);

        logger.info("PersonInfo success : {} persons returned with lastName {}", personInfoByLastName.size(), lastName);
        return personInfoByLastName;

    }
}
