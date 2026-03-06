package com.openclassrooms.safetynetalerts.service.endpoint;

import com.openclassrooms.safetynetalerts.dto.FloodHouseholdDTO;
import com.openclassrooms.safetynetalerts.dto.FloodPersonDTO;
import com.openclassrooms.safetynetalerts.mapper.FloodMapper;
import com.openclassrooms.safetynetalerts.model.Firestation;
import com.openclassrooms.safetynetalerts.model.MedicalRecord;
import com.openclassrooms.safetynetalerts.model.Person;
import com.openclassrooms.safetynetalerts.repository.DataLoader;
import com.openclassrooms.safetynetalerts.utils.AgeUtils;
import com.openclassrooms.safetynetalerts.utils.StringNormalizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Implémentation du service métier pour l'endpoint {@code /flood/stations}.
 *
 * <p>Fonctionnement :</p>
 * <ul>
 *   <li>valide la liste des stations,</li>
 *   <li>déduit les adresses couvertes par ces stations,</li>
 *   <li>construit un index des dossiers médicaux,</li>
 *   <li>regroupe les personnes par adresse (foyers),</li>
 *   <li>construit la réponse sous forme de {@link FloodHouseholdDTO}.</li>
 * </ul>
 *
 *
 * <p>Comportements importants :</p>
 * <ul>
 *   <li>les stations null/blanches sont ignorées,</li>
 *   <li>les adresses sont normalisées pour les comparaisons,</li>
 *   <li>si l'âge n'est pas calculable, il est renseigné à {@code 0}.</li>
 * </ul>
 *
 * @since 1.0
 */
@Service
public class FloodService implements IFloodService {

    private static final Logger logger = LoggerFactory.getLogger(FloodService.class);

    private final FloodMapper floodMapper;

    /**
     * Construit le service Flood.
     *
     * @param floodMapper mapper MapStruct utilisé pour construire les DTO
     * @since 1.0
     */
    public FloodService(FloodMapper floodMapper) {
        this.floodMapper = floodMapper;
    }

    /**
     * Retourne les foyers couverts par les stations fournies.
     *
     * <p>Si {@code stations} est null/vide ou si aucune adresse n'est couverte,
     * retourne une liste vide.</p>
     *
     * @param stations liste des numéros de station
     * @return liste des {@link FloodHouseholdDTO} (peut être vide)
     * @since 1.0
     */
    public List<FloodHouseholdDTO> getHouseholdsByStations(List<String> stations) {
        logger.debug("Flood request: stations={}", stations);

        if (stations == null || stations.isEmpty()) {
            logger.debug("Flood rejected: stations is null/empty");
            return List.of();
        }

        Set<String> stationSet = new HashSet<>();
        for (String s : stations) {
            if (s == null || s.isBlank()) continue;
            stationSet.add(StringNormalizer.norm(s));
        }

        Set<String> coveredAddresses = findCoveredAddresses(stationSet);
        logger.debug("Flood: {} covered addresses found for stations={}", coveredAddresses.size(), stationSet);

        if (coveredAddresses.isEmpty()) {
            logger.debug("Flood: no covered addresses found");
            return List.of();
        }

        Map<String, MedicalRecord> medicalIndex = buildMedicalIndex();
        logger.debug("Flood: medical index built with {} records", medicalIndex.size());

        Map<String, List<Person>> personsByAddress = groupPersons(coveredAddresses);
        logger.debug("Flood: {} household(s) matched", personsByAddress.size());

        if (personsByAddress.isEmpty()) {
            return List.of();
        }

        List<FloodHouseholdDTO> households = new ArrayList<>();

        for (Map.Entry<String, List<Person>> entry : personsByAddress.entrySet()) {
            String addressKey = entry.getKey();
            List<Person> persons = entry.getValue();

            List<FloodPersonDTO> residents = buildResidents(persons, medicalIndex);

            FloodHouseholdDTO household = new FloodHouseholdDTO();
            household.setAddress(addressKey);
            household.setResidents(residents);

            households.add(household);
        }

        logger.debug("Flood: response built with {} household(s)", households.size());
        return households;
    }

// ----------------------- HELPERS ------------------------------

    /**
     * Construit l'ensemble des adresses couvertes par les stations fournies.
     *
     * <p>Les numéros de station sont comparés après {@code trim()}.
     * Les adresses retournées sont normalisées (trim + lowercase).</p>
     *
     * @param stationSet ensemble des stations filtrées (non null)
     * @return ensemble d'adresses couvertes (jamais {@code null})
     */
    private Set<String> findCoveredAddresses(Set<String> stationSet) {
        Set<String> coveredAddresses = new HashSet<>();

        List<Firestation> firestations = DataLoader.DATASOURCE.getFirestations();
        for (Firestation fs : firestations) {
            if (fs == null || fs.getStation() == null || fs.getAddress() == null) continue;

            String st = StringNormalizer.norm(fs.getStation());

            if (!stationSet.contains(st)) continue;

            if (!fs.getAddress().isBlank()) {
                coveredAddresses.add(StringNormalizer.norm(fs.getAddress()));
            }
        }
        return coveredAddresses;
    }

    /**
     * Construit un index des dossiers médicaux basé sur la clé "prenom|nom" normalisée.
     *
     * <p>En cas de doublon de clé, le dernier dossier rencontré écrase le précédent.</p>
     *
     * @return map clé personne → {@link MedicalRecord}
     */
    private Map<String, MedicalRecord> buildMedicalIndex() {
        Map<String, MedicalRecord> medicalIndex = new HashMap<>();

        for (MedicalRecord mr : DataLoader.DATASOURCE.getMedicalrecords()) {
            if (mr == null) continue;
            String key = personKey(mr.getFirstName(), mr.getLastName());
            medicalIndex.put(key, mr);
        }
        return medicalIndex;
    }

    /**
     * Regroupe les personnes par adresse parmi les adresses couvertes.
     *
     * <p>La clé de regroupement est l'adresse normalisée (trim + lowercase).</p>
     *
     * @param coveredAddresses ensemble des adresses couvertes (non null)
     * @return map adresse → résidents (peut être vide)
     */
    private Map<String, List<Person>> groupPersons(Set<String> coveredAddresses) {
        Map<String, List<Person>> result = new HashMap<>();

        for (Person p : DataLoader.DATASOURCE.getPersons()) {
            if (p == null || p.getAddress() == null) continue;

            String addrKey = StringNormalizer.norm(p.getAddress());
            if (!coveredAddresses.contains(addrKey)) continue;

            if (!result.containsKey(addrKey)) {
                result.put(addrKey, new ArrayList<>());
            }
            result.get(addrKey).add(p);
        }
        return result;
    }

    /**
     * Calcule l'âge d'une personne à partir de son dossier médical.
     *
     * <p>Retourne {@code 0} si le dossier médical est absent, si la date de naissance est absente/blanche,
     * ou si le calcul échoue.</p>
     *
     * @param p  personne concernée
     * @param mr dossier médical (peut être null)
     * @return âge en années, ou {@code 0} si non calculable
     */
    private int resolveAge(Person p, MedicalRecord mr) {
        if (mr == null || mr.getBirthdate() == null || mr.getBirthdate().isBlank()) {
            return 0;
        }
        try {
            return AgeUtils.calculateAge(mr.getBirthdate());
        } catch (Exception e) {
            logger.error("Flood: cannot parse birthdate='{}' for {} {}",
                    mr.getBirthdate(), p.getFirstName(), p.getLastName(), e);
            return 0;
        }
    }

    /**
     * Construit la liste des résidents d'un foyer sous forme de {@link FloodPersonDTO}.
     *
     * <p>Pour chaque personne, tente d'associer un {@link MedicalRecord} via l'index
     * et calcule l'âge. Si non calculable, l'âge est renseigné à {@code 0}.</p>
     *
     * @param persons      résidents du foyer
     * @param medicalIndex index des dossiers médicaux
     * @return liste des résidents (jamais {@code null})
     */
    private List<FloodPersonDTO> buildResidents(List<Person> persons, Map<String, MedicalRecord> medicalIndex) {
        List<FloodPersonDTO> residents = new ArrayList<>();

        for (Person p : persons) {
            if (p == null) continue;

            MedicalRecord mr = medicalIndex.get(personKey(p.getFirstName(), p.getLastName()));
            int age = resolveAge(p, mr);

            FloodPersonDTO dto = floodMapper.toFloodPersonDTO(p, mr, age);
            if (dto != null) {
                residents.add(dto);
            }
        }
        return residents;
    }

    /**
     * Construit une clé d'index basée sur prénom et nom, après normalisation.
     *
     * @param firstName prénom (peut être null)
     * @param lastName  nom (peut être null)
     * @return clé sous la forme {@code prenom|nom}
     */
    private String personKey(String firstName, String lastName) {
        return (firstName == null ? "" : StringNormalizer.norm(firstName))
                + "|"
                + (lastName == null ? "" : StringNormalizer.norm(lastName));
    }
}
