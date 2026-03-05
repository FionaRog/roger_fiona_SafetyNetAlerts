package com.openclassrooms.safetynetalerts.service;

import com.openclassrooms.safetynetalerts.dto.FirestationResponseDTO;
import com.openclassrooms.safetynetalerts.model.Firestation;

import java.util.List;

/**
 * Contrat du service de gestion des casernes (firestations).
 *
 * <p>Ce service fournit :</p>
 * <ul>
 *   <li>des opérations CRUD sur les mappings adresse ↔ station,</li>
 *   <li>la logique métier de l'endpoint {@code /firestation?stationNumber=...}.</li>
 * </ul>
 *
 *
 * @since 1.0
 */
public interface IFirestationService {

    /**
     * Retourne les personnes couvertes par une station ainsi que le nombre d'adultes et d'enfants.
     *
     * @param firestation numéro de station (peut être null ou blanc)
     * @return {@link FirestationResponseDTO} résultat (peut être vide)
     * @since 1.0
     */
    FirestationResponseDTO getPersonsByFirestation(String firestation);

    /**
     * Retourne la liste des mappings adresse ↔ station.
     *
     * @return liste des {@link Firestation} (copie de la source)
     * @since 1.0
     */
    List<Firestation> getFirestation();

    /**
     * Ajoute un mapping adresse ↔ station.
     *
     * <p>Rejet si l'adresse ou la station est null/blanche, ou si un mapping existe déjà pour l'adresse.</p>
     *
     * @param newfirestation mapping à ajouter
     * @return {@code true} si ajouté, {@code false} sinon
     * @since 1.0
     */
    boolean addFirestation(Firestation newfirestation);

    /**
     * Met à jour la station associée à une adresse.
     *
     * <p>Rejet si l'adresse ou la station est null/blanche, ou si aucun mapping n'existe pour l'adresse.</p>
     *
     * @param updatedFirestation mapping contenant l'adresse cible et la nouvelle station
     * @return {@code true} si mis à jour, {@code false} sinon
     * @since 1.0
     */
    boolean updateFirestationByAddress(Firestation updatedFirestation);

    /**
     * Supprime le mapping correspondant à l'adresse fournie.
     *
     * @param deletedAddress adresse à supprimer (peut être null/blanche)
     * @return {@code true} si au moins un mapping a été supprimé, {@code false} sinon
     * @since 1.0
     */
    boolean deleteFirestationByAddress(String deletedAddress);

    /**
     * Supprime tous les mappings correspondant à une station.
     *
     * @param deletedStation station à supprimer (peut être null/blanche)
     * @return {@code true} si au moins un mapping a été supprimé, {@code false} sinon
     * @since 1.0
     */
    boolean deleteFirestationByStation(String deletedStation);
}

