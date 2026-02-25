package com.openclassrooms.safetynetalerts.controller.crud;

import com.openclassrooms.safetynetalerts.model.Firestation;
import com.openclassrooms.safetynetalerts.service.IFirestationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class FirestationCrudController {


    private final IFirestationService firestationService;

    public FirestationCrudController (IFirestationService firestationService) {
        this.firestationService = firestationService;
    }


    @GetMapping(value= "/firestation", params = "!stationNumber")
    public List<Firestation> getMapping() {

        return firestationService.getFirestation();
    }


    @PostMapping("/firestation")
    public ResponseEntity<Void> addMapping(@RequestBody Firestation firestation) {

        boolean added = firestationService.addFirestation(firestation);
        return added
                ? ResponseEntity.status(HttpStatus.CREATED).build()
                : ResponseEntity.status(HttpStatus.CONFLICT).build();
    }


    @PutMapping("/firestation")
    public ResponseEntity<Void> updateMapping(@RequestBody Firestation firestation) {

        boolean updated = firestationService.updateFirestationByAddress(firestation);
        return updated
                ? ResponseEntity.noContent().build()
                : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }


    @DeleteMapping(value = "/firestation", params= "address")
    public ResponseEntity<Void> deleteByAdress(@RequestParam String address) {

        boolean deleted = firestationService.deleteFirestationByAddress(address);
        return deleted
                ? ResponseEntity.noContent().build()
                : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }


    @DeleteMapping(value = "/firestation", params = "station")
    public ResponseEntity<Void> deleteByStation(@RequestParam String station) {

        boolean deleted = firestationService.deleteFirestationByStation(station);
        return deleted
                ? ResponseEntity.noContent().build()
                : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}
