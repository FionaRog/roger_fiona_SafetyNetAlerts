package com.openclassrooms.SafetyNetAlerts.Controller;

import com.openclassrooms.SafetyNetAlerts.Dto.FirestationResponseDTO;
import com.openclassrooms.SafetyNetAlerts.Model.Firestation;
import com.openclassrooms.SafetyNetAlerts.Service.FirestationCrudService;
import com.openclassrooms.SafetyNetAlerts.Service.FirestationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class FirestationCrudController {


    private final FirestationService firestationService;
    private final FirestationCrudService firestationCrudService;

    public FirestationCrudController (FirestationService firestationService,
                                      FirestationCrudService firestationCrudService) {
        this.firestationService = firestationService;
        this.firestationCrudService = firestationCrudService;
    }

    // ? ajout d'un Get ?
    @GetMapping("/firestation")
    public FirestationResponseDTO getByStation(@RequestParam("stationNumber") String stationNumber) {
        return firestationService.getPersonsByFirestation(stationNumber);
    }

    @PostMapping("/firestation")
    public ResponseEntity<Void> addMapping(@RequestBody Firestation firestation) {
        boolean added = firestationCrudService.addMapping(firestation);
        return added
                ? ResponseEntity.status(HttpStatus.CREATED).build()
                : ResponseEntity.status(HttpStatus.CONFLICT).build();
    }

    @PutMapping("/firestation")
    public ResponseEntity<Void> updateMapping(@RequestBody Firestation firestation) {
        boolean updated = firestationCrudService.updateStationByAddress(firestation);
        return updated
                ? ResponseEntity.noContent().build()
                : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @DeleteMapping(value = "/firestation", params= "address")
    public ResponseEntity<Void> deleteByAdress(@RequestParam String address) {
        boolean deleted = firestationCrudService.deleteByAddress(address);
        return deleted
                ? ResponseEntity.noContent().build()
                : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @DeleteMapping(value = "/firestation", params = "station")
    public ResponseEntity<Void> deleteByStation(@RequestParam String station) {
        boolean deleted = firestationCrudService.deleteByStation(station);
        return deleted
                ? ResponseEntity.noContent().build()
                : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}
