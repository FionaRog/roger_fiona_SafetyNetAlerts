package com.openclassrooms.safetynetalerts.service.endpoint;

import com.openclassrooms.safetynetalerts.dto.ChildAlertDTO;

import java.util.List;

public interface IChildAlertService {

    List<ChildAlertDTO> getChildByAddress(String address);
}
