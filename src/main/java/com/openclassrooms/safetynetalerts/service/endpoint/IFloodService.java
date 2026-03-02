package com.openclassrooms.safetynetalerts.service.endpoint;

import com.openclassrooms.safetynetalerts.dto.FloodHouseholdDTO;

import java.util.List;

public interface IFloodService {

    List<FloodHouseholdDTO> getHouseholdsByStations(List<String> stations);

}
