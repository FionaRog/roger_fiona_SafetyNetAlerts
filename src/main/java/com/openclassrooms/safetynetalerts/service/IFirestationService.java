package com.openclassrooms.safetynetalerts.service;

import com.openclassrooms.safetynetalerts.dto.FirestationResponseDTO;
import com.openclassrooms.safetynetalerts.model.Firestation;

import java.util.List;

public interface IFirestationService {

    FirestationResponseDTO getPersonsByFirestation(String firestation);

    List<Firestation> getFirestation();

    boolean addFirestation(Firestation newfirestation);

    boolean updateFirestationByAddress(Firestation updatedFirestation);

    boolean deleteFirestationByAddress(String deletedAddress);

    boolean deleteFirestationByStation(String deletedStation);
}

