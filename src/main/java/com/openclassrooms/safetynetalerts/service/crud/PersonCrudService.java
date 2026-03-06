package com.openclassrooms.safetynetalerts.service.crud;

import com.openclassrooms.safetynetalerts.model.Person;
import com.openclassrooms.safetynetalerts.repository.DataLoader;
import com.openclassrooms.safetynetalerts.utils.StringNormalizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Implémentation du service CRUD permettant de gérer les {@link Person}.
 *
 * <p>Les données sont manipulées en mémoire via {@code DataLoader.DATASOURCE}.</p>
 *
 * <p>Règles principales :</p>
 * <ul>
 *   <li>une personne est identifiée par le couple prénom + nom (comparaison après normalisation (trim + lowercase)),</li>
 *   <li>l'ajout est refusé si un doublon existe,</li>
 *   <li>la mise à jour remplace les champs d'adresse et de contact,</li>
 *   <li>la suppression retire l'entrée correspondante si elle existe.</li>
 * </ul>
 *
 * @since 1.0
 */
@Service
public class PersonCrudService implements IPersonCrudService {

    private static final Logger logger = LoggerFactory.getLogger(PersonCrudService.class);

    /**
     * Retourne l'ensemble des personnes.
     *
     * @return liste des {@link Person} (copie de la source)
     * @since 1.0
     */
    public List<Person> getPerson() {
        return new ArrayList<>(DataLoader.DATASOURCE.getPersons());
    }

    /**
     * Ajoute une nouvelle personne.
     *
     * <p>Refuse l'ajout si une personne existe déjà avec le même prénom et nom
     * (comparaison après normalisation (trim + lowercase)).</p>
     *
     * @param newPerson personne à ajouter
     * @return {@code true} si ajoutée, {@code false} sinon
     * @since 1.0
     */
    public boolean addPerson(Person newPerson) {
        if (newPerson == null || newPerson.getFirstName() == null || newPerson.getLastName() == null) {
            return false;
        }

        for (Person person : DataLoader.DATASOURCE.getPersons()) {
            if (person == null) continue;

            if (StringNormalizer.same(person.getFirstName(), newPerson.getFirstName())
                    && StringNormalizer.same(person.getLastName(), newPerson.getLastName())) {

                logger.debug("Add person rejected : duplicate for {} {} ",
                        newPerson.getFirstName(), newPerson.getLastName());
                return false;
            }
        }

        DataLoader.DATASOURCE.getPersons().add(newPerson);

        logger.debug("new person {} {} added ", newPerson.getFirstName(), newPerson.getLastName());
        return true;
    }

    /**
     * Met à jour une personne existante identifiée par prénom + nom.
     *
     * <p>Champs mis à jour :</p>
     * <ul>
     *   <li>adresse</li>
     *   <li>ville</li>
     *   <li>code postal</li>
     *   <li>téléphone</li>
     *   <li>email</li>
     * </ul>
     *
     * @param updatedPerson personne contenant les champs mis à jour
     * @return {@code true} si mise à jour effectuée, {@code false} sinon
     * @since 1.0
     */
    public boolean updatePerson(Person updatedPerson) {
        if (updatedPerson == null || updatedPerson.getFirstName() == null || updatedPerson.getLastName() == null) {
            return false;
        }

        for (Person person : DataLoader.DATASOURCE.getPersons()) {
            if (person == null) continue;

            if (StringNormalizer.same(person.getFirstName(), updatedPerson.getFirstName())
                    && StringNormalizer.same(person.getLastName(), updatedPerson.getLastName())) {

                person.setAddress(updatedPerson.getAddress());
                person.setCity(updatedPerson.getCity());
                person.setZip(updatedPerson.getZip());
                person.setPhone(updatedPerson.getPhone());
                person.setEmail(updatedPerson.getEmail());

                logger.debug("person {} {} updated",
                        person.getFirstName(), person.getLastName());
                return true;
            }
        }

        logger.debug("Update person for {} {} rejected, not found",
                updatedPerson.getFirstName(), updatedPerson.getLastName());
        return false;
    }

    /**
     * Supprime une personne à partir du prénom et du nom.
     *
     * <p>Si {@code firstName} ou {@code lastName} est null/blanc, la suppression est refusée.</p>
     *
     * @param firstName prénom de la personne
     * @param lastName  nom de famille de la personne
     * @return {@code true} si suppression effectuée, {@code false} sinon
     * @since 1.0
     */
    public boolean deletePerson(String firstName, String lastName) {

        if (firstName == null || firstName.isBlank() || lastName == null || lastName.isBlank()) {
            logger.debug("Delete person rejected : firstName/LastName is null/blank");
            return false;
        }
        int before = DataLoader.DATASOURCE.getPersons().size();

        DataLoader.DATASOURCE.getPersons().removeIf(p ->
                p != null
                        && p.getFirstName() != null
                        && p.getLastName() != null
                        && StringNormalizer.same(p.getFirstName(), firstName)
                        && StringNormalizer.same(p.getLastName(), lastName)
        );

        int after = DataLoader.DATASOURCE.getPersons().size();

        boolean deleted = after < before;

        if (deleted) {
            logger.debug("person {} {} deleted", firstName, lastName);
        } else {
            logger.debug("Delete person {} {} rejected", firstName, lastName);
        }

        return deleted;
    }
}
