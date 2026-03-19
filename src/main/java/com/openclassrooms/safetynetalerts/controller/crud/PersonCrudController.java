package com.openclassrooms.safetynetalerts.controller.crud;

import com.openclassrooms.safetynetalerts.model.Person;
import com.openclassrooms.safetynetalerts.service.crud.IPersonCrudService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Contrôleur REST exposant les opérations CRUD sur les personnes.
 *
 * <p>Endpoints exposés :</p>
 * <ul>
 *   <li>{@code GET /person}</li>
 *   <li>{@code POST /person}</li>
 *   <li>{@code PUT /person}</li>
 *   <li>{@code DELETE /person?firstName=...&lastName=...}</li>
 * </ul>
 *
 * <p>Responsabilité :
 * adapter les requêtes HTTP vers le service {@link IPersonCrudService}
 * et retourner les codes HTTP appropriés.</p>
 *
 * @since 1.0
 */
@RestController
public class PersonCrudController {

    private static final Logger logger = LoggerFactory.getLogger(PersonCrudController.class);
    private final IPersonCrudService personCrudService;

    /**
     * Construit le contrôleur CRUD Person.
     *
     * @param personCrudService service métier associé
     * @since 1.0
     */
    public PersonCrudController(IPersonCrudService personCrudService) {
        this.personCrudService = personCrudService;
    }

    /**
     * Retourne l'ensemble des personnes.
     *
     * <p>Code HTTP : {@code 200 OK}.</p>
     *
     * @return liste des {@link Person}
     * @since 1.0
     */
    @GetMapping("/person")
    public List<Person> getPerson() {
        logger.info("Received request GET /person");
        return personCrudService.getPerson();
    }

    /**
     * Ajoute une nouvelle personne.
     *
     * <p>Codes HTTP :</p>
     * <ul>
     *   <li>{@code 201 Created} si la personne est ajoutée</li>
     *   <li>{@code 409 Conflict} si la personne existe déjà ou si les données sont invalides</li>
     * </ul>
     *
     * @param person personne à ajouter
     * @return réponse HTTP sans corps
     * @since 1.0
     */
    @PostMapping("/person")
    public ResponseEntity<Void> addPerson(@RequestBody Person person) {
        logger.info("POST /person - request received: {}", person);
        boolean added = personCrudService.addPerson(person);
        return added
                ? ResponseEntity.status(HttpStatus.CREATED).build()
                : ResponseEntity.status(HttpStatus.CONFLICT).build();
    }

    /**
     * Met à jour une personne existante.
     *
     * <p>Codes HTTP :</p>
     * <ul>
     *   <li>{@code 204 No Content} si la mise à jour réussit</li>
     *   <li>{@code 404 Not Found} si aucune personne ne correspond</li>
     * </ul>
     *
     * @param person personne contenant les informations mises à jour
     * @return réponse HTTP sans corps
     * @since 1.0
     */
    @PutMapping("/person")
    public ResponseEntity<Void> updatePerson(@RequestBody Person person) {
        logger.info("PUT /person - request received: {}", person);
        boolean updated = personCrudService.updatePerson(person);
        return updated
                ? ResponseEntity.noContent().build()
                : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    /**
     * Supprime une personne à partir du prénom et du nom.
     *
     * <p>Codes HTTP :</p>
     * <ul>
     *   <li>{@code 204 No Content} si suppression effectuée</li>
     *   <li>{@code 404 Not Found} si aucune personne ne correspond</li>
     * </ul>
     *
     * @param firstName prénom de la personne
     * @param lastName  nom de famille de la personne
     * @return réponse HTTP sans corps
     * @since 1.0
     */
    @DeleteMapping("/person")
    public ResponseEntity<Void> deletePerson(@RequestParam String firstName, @RequestParam String lastName) {
        logger.info("DELETE /person - request received for: {} {}", firstName, lastName);
        boolean deleted = personCrudService.deletePerson(firstName, lastName);
        return deleted
                ? ResponseEntity.noContent().build()
                : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}
