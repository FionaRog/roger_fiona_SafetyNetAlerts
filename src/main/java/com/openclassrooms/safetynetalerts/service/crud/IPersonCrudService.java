package com.openclassrooms.safetynetalerts.service.crud;

import com.openclassrooms.safetynetalerts.model.Person;

import java.util.List;

/**
 * Contrat du service CRUD permettant de gérer les {@link Person}.
 *
 * <p>Ce service fournit les opérations de lecture, ajout, mise à jour et suppression
 * des personnes dans la source de données en mémoire.</p>
 *
 * @since 1.0
 */
public interface IPersonCrudService {

    /**
     * Retourne l'ensemble des personnes.
     *
     * @return liste des {@link Person}
     * @since 1.0
     */
    List<Person> getPerson();

    /**
     * Ajoute une nouvelle personne.
     *
     * <p>Rejet si une personne existe déjà avec le même prénom et nom (comparaison après normalisation (trim + lowercase)).</p>
     *
     * @param newPerson personne à ajouter
     * @return {@code true} si ajoutée, {@code false} sinon
     * @since 1.0
     */
    boolean addPerson(Person newPerson);

    /**
     * Met à jour une personne existante.
     *
     * <p>La personne à mettre à jour est identifiée par prénom + nom (comparaison après normalisation (trim + lowercase)).</p>
     *
     * @param updatedPerson personne contenant les champs mis à jour
     * @return {@code true} si mise à jour effectuée, {@code false} sinon
     * @since 1.0
     */
    boolean updatePerson(Person updatedPerson);

    /**
     * Supprime une personne à partir du prénom et du nom.
     *
     * @param firstName prénom de la personne
     * @param lastName  nom de famille de la personne
     * @return {@code true} si suppression effectuée, {@code false} sinon
     * @since 1.0
     */
    boolean deletePerson(String firstName, String lastName);
}
