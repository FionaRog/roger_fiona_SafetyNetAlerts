package com.openclassrooms.safetynetalerts.service.endpoint;

import com.openclassrooms.safetynetalerts.dto.PersonInfoDTO;

import java.util.List;

public interface IPersonInfoService {

    List<PersonInfoDTO> getPersonsByLastName (String lastName);
}
