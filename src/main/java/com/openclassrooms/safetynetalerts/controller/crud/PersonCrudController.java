package com.openclassrooms.safetynetalerts.controller.crud;

import com.openclassrooms.safetynetalerts.model.Person;
import com.openclassrooms.safetynetalerts.service.crud.IPersonCrudService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class PersonCrudController {

    private final IPersonCrudService personCrudService;

    public PersonCrudController(IPersonCrudService personCrudService) {
        this.personCrudService = personCrudService;
    }

    @GetMapping("/person")
    public List<Person> getPerson() {

        return personCrudService.getPerson();
    }


    @PostMapping("/person")
    public ResponseEntity<Void> addPerson(@RequestBody Person person) {

        boolean added = personCrudService.addPerson(person);
        return added
                ? ResponseEntity.status(HttpStatus.CREATED).build()
                : ResponseEntity.status(HttpStatus.CONFLICT).build();
    }


    @PutMapping("/person")
    public ResponseEntity<Void> updatePerson(@RequestBody Person person) {

        boolean updated = personCrudService.updatePerson(person);
        return updated
                ? ResponseEntity.noContent().build()
                : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }


    @DeleteMapping("/person")
    public ResponseEntity<Void> deletePerson(@RequestParam String firstName, @RequestParam String lastName) {

        boolean deleted = personCrudService.deletePerson(firstName, lastName);
        return deleted
                ? ResponseEntity.noContent().build()
                : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}
