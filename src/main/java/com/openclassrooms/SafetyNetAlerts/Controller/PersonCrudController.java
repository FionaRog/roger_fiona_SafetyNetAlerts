package com.openclassrooms.SafetyNetAlerts.Controller;

import com.openclassrooms.SafetyNetAlerts.Model.Person;
import com.openclassrooms.SafetyNetAlerts.Service.PersonCrudService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class PersonCrudController {

    private final PersonCrudService personCrudService;

    public PersonCrudController(PersonCrudService personCrudService) {
        this.personCrudService = personCrudService;
    }

    // Ajouter ? :
    @PostMapping("/person")
    public ResponseEntity<Void> addPerson(@RequestBody Person person) {
        boolean added = personCrudService.addPerson(person);

        if(!added) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // Ajouter ? :
    @PutMapping("/person")
    public ResponseEntity<Void> updatePerson(@RequestBody Person person) {
        boolean updated = personCrudService.updatePerson(person);

        if(!updated) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        return ResponseEntity.noContent().build();
    }

    // Ajouter ? :
    @DeleteMapping("/person")
    public ResponseEntity<Void> deletePerson(
            @RequestParam String firstName,
            @RequestParam String lastName) {

        boolean deleted = personCrudService.deletePerson(firstName, lastName);

        if(!deleted) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        return ResponseEntity.noContent().build();
    }
}
