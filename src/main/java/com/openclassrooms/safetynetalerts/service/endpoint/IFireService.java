package com.openclassrooms.safetynetalerts.service.endpoint;

import com.openclassrooms.safetynetalerts.dto.FireResponseDTO;

public interface IFireService {

    FireResponseDTO getPersonByAddress (String address);
}
