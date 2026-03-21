package com.openclassrooms.safetynetalerts.service;

import com.openclassrooms.safetynetalerts.dto.FirestationPersonDTO;
import com.openclassrooms.safetynetalerts.dto.FirestationResponseDTO;
import com.openclassrooms.safetynetalerts.mapper.FirestationMapper;
import com.openclassrooms.safetynetalerts.model.Firestation;
import com.openclassrooms.safetynetalerts.model.MedicalRecord;
import com.openclassrooms.safetynetalerts.model.Person;
import com.openclassrooms.safetynetalerts.repository.DataLoader;
import com.openclassrooms.safetynetalerts.repository.DataPersistenceService;
import com.openclassrooms.safetynetalerts.utils.AgeUtils;
import com.openclassrooms.safetynetalerts.utils.StringNormalizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Implémentation du service de gestion des casernes (firestations).
 *
 * <p>Cette classe combine :</p>
 * <ul>
 *   <li>des opérations CRUD sur les mappings adresse ↔ station,</li>
 *   <li>la logique métier de l'endpoint {@code /firestation?stationNumber=...}.</li>
 * </ul>
 *
 *
 * <p>Règles principales pour l'endpoint :</p>
 * <ul>
 *   <li>une station couvre un ensemble d'adresses,</li>
 *   <li>les personnes vivant à ces adresses sont incluses dans la réponse,</li>
 *   <li>le calcul enfant/adulte repose sur l'âge (enfant {@code <= 18}), si l'âge est calculable.</li>
 * </ul>
 *
 * @since 1.0
 */
@Service
public class FirestationService implements IFirestationService {

    private static final Logger logger = LoggerFactory.getLogger(FirestationService.class);

    private final DataPersistenceService dataPersistenceService;
    private final FirestationMapper firestationMapper;

    /**
     * Construit le service Firestation.
     *
     * @param firestationMapper mapper MapStruct utilisé pour construire les DTO
     * @param dataPersistenceService service responsable de la persistance des données
     * dans le fichier JSON externe
     *
     * @since 1.0
     */
    public FirestationService(FirestationMapper firestationMapper, DataPersistenceService dataPersistenceService) {
                this.firestationMapper = firestationMapper;
                this.dataPersistenceService = dataPersistenceService;
    }

// --------------- CRUD ----------------------

    /**
     * Retourne la liste des mappings adresse ↔ station.
     *
     * @return liste des {@link Firestation} (copie)
     * @since 1.0
     */
    public List<Firestation> getFirestation() {
        return new ArrayList<>(DataLoader.FIRESTATIONS);
    }

    /**
     * Ajoute un mapping adresse ↔ station.
     *
     * @param newfirestation mapping à ajouter
     * @return {@code true} si ajouté, {@code false} si paramètres invalides ou duplicat d'adresse
     * @since 1.0
     */
    public boolean addFirestation(Firestation newfirestation) {

        if (newfirestation == null || newfirestation.getAddress() == null || newfirestation.getAddress().isBlank())
            return false;
        if (newfirestation.getStation() == null || newfirestation.getStation().isBlank()) return false;

        for (Firestation firestation : DataLoader.FIRESTATIONS) {
            if (StringNormalizer.same(firestation.getAddress(), newfirestation.getAddress())) {
                logger.debug("Add firestation rejected : duplicate for address {} ", firestation.getAddress());
                return false;
            }
        }
        DataLoader.FIRESTATIONS.add(newfirestation);
        dataPersistenceService.saveData();
        logger.debug("firestation added with address {} and station {}",
                newfirestation.getAddress(), newfirestation.getStation());
        return true;
    }

    /**
     * Met à jour la station associée à une adresse.
     *
     * @param updatedFirestation mapping avec adresse cible et nouvelle station
     * @return {@code true} si mis à jour, {@code false} si paramètres invalides ou adresse introuvable
     * @since 1.0
     */
    public boolean updateFirestationByAddress(Firestation updatedFirestation) {

        if (updatedFirestation == null || updatedFirestation.getAddress() == null || updatedFirestation.getAddress().isBlank())
            return false;
        if (updatedFirestation.getStation() == null || updatedFirestation.getStation().isBlank()) return false;

        for (Firestation firestation : DataLoader.FIRESTATIONS) {
            if (StringNormalizer.same(firestation.getAddress(), updatedFirestation.getAddress())) {

                firestation.setStation(updatedFirestation.getStation());
                dataPersistenceService.saveData();

                logger.debug("firestation updated for address {} with updated station {}",
                        updatedFirestation.getAddress(), updatedFirestation.getStation());
                return true;
            }
        }
        logger.debug("Update firestation rejected for address {}", updatedFirestation.getAddress());
        return false;
    }

    /**
     * Supprime le mapping correspondant à l'adresse fournie.
     *
     * @param deletedAddress adresse à supprimer
     * @return {@code true} si suppression effectuée, {@code false} sinon
     * @since 1.0
     */
    public boolean deleteFirestationByAddress(String deletedAddress) {

        if (deletedAddress == null || deletedAddress.isBlank()) {
            logger.debug("Delete firestation rejected : address is null/blank");
            return false;
        }

        int before = DataLoader.FIRESTATIONS.size();
        DataLoader.FIRESTATIONS
                .removeIf(fs ->
                        fs.getAddress() != null &&
                                StringNormalizer.same(fs.getAddress(), deletedAddress));

        int after = DataLoader.FIRESTATIONS.size();

        boolean deleted = after < before;

        if (deleted) {
            dataPersistenceService.saveData();
            logger.debug("Firestation for address '{}' deleted", deletedAddress);
        } else {
            logger.debug("Delete firestation rejected: no mapping found for address '{}'", deletedAddress);
        }

        return deleted;
    }

    /**
     * Supprime tous les mappings correspondant à la station fournie.
     *
     * @param deletedStation station à supprimer
     * @return {@code true} si suppression effectuée, {@code false} sinon
     * @since 1.0
     */
    public boolean deleteFirestationByStation(String deletedStation) {

        if (deletedStation == null || deletedStation.isBlank()) {
            logger.debug("Delete firestation rejected, station is null/blank");
            return false;
        }

        int before = DataLoader.FIRESTATIONS.size();
        DataLoader.FIRESTATIONS.
                removeIf(fs ->
                        fs.getStation() != null &&
                                StringNormalizer.same(fs.getStation(), deletedStation));

        int after = DataLoader.FIRESTATIONS.size();

        boolean deleted = after < before;

        if (deleted) {
            dataPersistenceService.saveData();
            logger.debug("Firestation for station '{}' deleted", deletedStation);
        } else {
            logger.debug("Delete firestation rejected: no mapping found for station '{}'", deletedStation);
        }

        return deleted;
    }

// ------------------- ENDPOINT ---------------------

    /**
     * Retourne les personnes couvertes par la station fournie ainsi que le nombre d'adultes et d'enfants.
     *
     * <p>Si {@code stationNumber} est null/blanc, si aucune adresse n'est couverte,
     * ou si aucune donnée exploitable n'est trouvée, un {@link FirestationResponseDTO} vide est retourné.</p>
     *
     * <p>Un enfant est défini comme ayant un âge {@code <= 18}.</p>
     *
     * @param stationNumber numéro de station
     * @return {@link FirestationResponseDTO} contenant la liste des personnes et les compteurs adultes/enfants
     * @since 1.0
     */
    public FirestationResponseDTO getPersonsByFirestation(String stationNumber) {
        logger.debug("Firestation request: station='{}'", stationNumber);

        if (stationNumber == null || stationNumber.isBlank()) {
            return new FirestationResponseDTO();
        }

        Set<String> coveredAddresses = findCoveredAddresses(stationNumber);

        logger.debug("Found {} covered addresses for station {}", coveredAddresses.size(), stationNumber);

        if (coveredAddresses.isEmpty()) {
            return new FirestationResponseDTO();
        }

        Map<String, MedicalRecord> medicalIndex = buildMedicalIndex();
        logger.debug("Medical index built with {} entries", medicalIndex.size());

        List<FirestationPersonDTO> people = new ArrayList<>();

        int[] counts = new int[2];

        fillPeopleAndCount(coveredAddresses, medicalIndex, people, counts);

        FirestationResponseDTO response = new FirestationResponseDTO();
        response.setPeople(people);
        response.setAdults(counts[0]);
        response.setChildren(counts[1]);

        return response;
    }


// ------------------ HELPERS -------------------

    /**
     * Construit l'ensemble des adresses couvertes par la station fournie.
     *
     * <p>La comparaison du numéro de station est réalisée après normalisation (trim + lowercase).</p>
     *
     * <p>Les adresses null ou blanches sont ignorées.
     * Les adresses retournées sont normalisées (trim + lowercase).</p>
     *
     * @param stationNumber numéro de station recherché
     * @return ensemble des adresses couvertes (jamais {@code null})
     */
    private Set<String> findCoveredAddresses(String stationNumber) {
        Set<String> coveredAddresses = new HashSet<>();

        for (Firestation fs : DataLoader.FIRESTATIONS) {
            if (fs.getStation() != null &&
                    StringNormalizer.same(fs.getStation(), stationNumber)) {

                if (fs.getAddress() != null && !fs.getAddress().isBlank()) {
                    coveredAddresses.add(StringNormalizer.norm(fs.getAddress()));
                }
            }
        }
        return coveredAddresses;
    }

    /**
     * Construit un index des dossiers médicaux basé sur la clé
     * "prenom|nom" normalisée.
     *
     * <p>Cet index permet un accès rapide au {@link MedicalRecord}
     * correspondant à une {@link Person}.</p>
     *
     * <p>En cas de doublon de clé, le dernier dossier rencontré écrase le précédent.</p>
     *
     * @return map associant une clé normalisée à un {@link MedicalRecord}
     */
    private Map<String, MedicalRecord> buildMedicalIndex() {

        Map<String, MedicalRecord> medicalIndex = new HashMap<>();

        for (MedicalRecord mr : DataLoader.MEDICAL_RECORDS) {
            if (mr == null) continue;

            String key = personKey(mr.getFirstName(), mr.getLastName());
            medicalIndex.put(key, mr);
        }
        return medicalIndex;
    }

    /**
     * Remplit la liste des personnes couvertes par les adresses fournies
     * et met à jour les compteurs adultes/enfants.
     *
     * <p>Pour chaque personne vivant à une adresse couverte :</p>
     * <ul>
     *   <li>un {@link FirestationPersonDTO} est ajouté à la liste {@code people},</li>
     *   <li>le compteur enfant est incrémenté si l'âge est {@code <= 18},</li>
     *   <li>sinon le compteur adulte est incrémenté.</li>
     * </ul>
     *
     *
     * <p>Les personnes sans âge calculable sont comptabilisées comme adultes.</p>
     *
     * @param coveredAddresses ensemble des adresses couvertes
     * @param medicalIndex     index des dossiers médicaux
     * @param people           liste des DTO à remplir
     * @param counts           tableau de taille 2 : index 0 = adultes, index 1 = enfants
     */
    private void fillPeopleAndCount(Set<String> coveredAddresses, Map<String, MedicalRecord> medicalIndex,
                                    List<FirestationPersonDTO> people, int[] counts) {
        for (Person person : DataLoader.PERSONS) {
            if (person.getAddress() == null) continue;

            String personAddress = StringNormalizer.norm(person.getAddress());

            if (!coveredAddresses.contains(personAddress)) continue;

            FirestationPersonDTO dto = firestationMapper.toFirestationPersonDTO(person);
            people.add(dto);

            Integer age = resolveAge(person, medicalIndex);

            if (age != null && age <= 18) {
                counts[1]++;
            } else counts[0]++;
        }
    }

    /**
     * Résout l'âge d'une personne à partir de l'index médical.
     *
     * <p>Retourne {@code null} si aucun dossier médical n'est trouvé
     * ou si la date de naissance est absente/invalide.</p>
     *
     * @param person       personne concernée
     * @param medicalIndex index des dossiers médicaux
     * @return âge en années, ou {@code null} si non calculable
     */
    private Integer resolveAge(Person person, Map<String, MedicalRecord> medicalIndex) {
        String key = personKey(person.getFirstName(), person.getLastName());
        MedicalRecord mr = medicalIndex.get(key);

        if (mr == null || mr.getBirthdate() == null || mr.getBirthdate().isBlank()) {
            logger.debug("No medicalRecord/birthdate for {} {}", person.getFirstName(), person.getLastName());
            return null;
        }

        try {
            return AgeUtils.calculateAge(mr.getBirthdate());
        } catch (IllegalArgumentException e) {
            logger.error("Invalid birthdate '{}' for {} {}",
                    mr.getBirthdate(), person.getFirstName(), person.getLastName(), e);
            return null;
        }
    }

    /**
     * Construit une clé d'identification unique basée sur le prénom et le nom.
     *
     * <p>Les deux valeurs sont normalisées puis concaténées
     * sous la forme {@code prenom|nom}.</p>
     *
     * @param firstName prénom (peut être null)
     * @param lastName  nom (peut être null)
     * @return clé normalisée utilisée pour l'index médical
     */
    private String personKey(String firstName, String lastName) {
        return StringNormalizer.norm(firstName)
                + "|"
                + StringNormalizer.norm(lastName);
    }

}
