package com.openclassrooms.safetynetalerts.service.endpoint;

import com.openclassrooms.safetynetalerts.dto.AdultDTO;
import com.openclassrooms.safetynetalerts.dto.ChildAlertDTO;
import com.openclassrooms.safetynetalerts.mapper.PersonMapper;
import com.openclassrooms.safetynetalerts.model.MedicalRecord;
import com.openclassrooms.safetynetalerts.model.Person;
import com.openclassrooms.safetynetalerts.repository.DataLoader;
import com.openclassrooms.safetynetalerts.utils.AgeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Implémentation du service "Child Alert".
 *
 * <p>Ce service :</p>
 * <ul>
 *   <li>valide et normalise l'adresse d'entrée,</li>
 *   <li>récupère les personnes vivant à cette adresse,</li>
 *   <li>calcule l'âge via le dossier médical,</li>
 *   <li>retourne les enfants (inférieur ou égale à 18 ans) avec la liste des adultes du foyer.</li>
 * </ul>
 *
 *
 * <p>Comportements importants :</p>
 * <ul>
 *   <li>retourne une liste vide si l'adresse est null/blanche, si aucun foyer n'est trouvé,
 *       si aucun enfant n'est présent</li>
 *   <li>ignore une personne si son âge n'est pas calculable (birthdate absente/invalide).</li>
 * </ul>
 *
 *
 * @since 1.0
 */
@Service
public class ChildAlertService implements IChildAlertService{


    private static final Logger logger = LoggerFactory.getLogger(ChildAlertService.class);

    private final PersonMapper personMapper;

    /**
     * Construit le service Child Alert.
     *
     * @param personMapper mapper MapStruct pour convertir les modèles en DTO
     * @since 1.0
     */
    public ChildAlertService(PersonMapper personMapper) {
        this.personMapper = personMapper;
    }

    /**
     * Point d'entrée métier : retourne les enfants (inférieur ou égale à 18 ans 18 ans) vivant à l'adresse donnée.
     *
     * <p>Chaque {@link ChildAlertDTO} retourné contient :</p>
     * <ul>
     *   <li>l'âge de l'enfant,</li>
     *   <li>la liste des membres adultes du foyer.</li>
     * </ul>
     *
     *
     * @param address adresse recherchée (peut être null ou blanche)
     * @return liste des enfants trouvés, ou liste vide si aucun enfant n'est trouvé
     * @since 1.0
     */
     public List<ChildAlertDTO> getChildByAddress (String address) {
         logger.debug("ChildAlert request: address='{}'", address);

        if(address == null || address.isBlank()) {
            logger.debug("ChildAlert rejected: address is null/blank");
            return List.of();
        }

        String targetAddress = normalize(address);

        Map<String, MedicalRecord> medicalIndex = buildMedicalIndex();

         List<Person> household = findHouseholdByAddress(targetAddress);
         logger.debug("ChildAlert: household size for address '{}' = {}",
                 targetAddress, household.size());

        if (household.isEmpty()) {
            logger.debug("ChildAlert: no household found for address '{}'", targetAddress);
            return List.of();
        }

        List<AdultDTO> adults = new ArrayList<>();
        List<ChildAlertDTO> children = new ArrayList<>();

        for (Person p : household) {
            MedicalRecord mr = medicalIndex.get(personKey(p.getFirstName(),p.getLastName()));

            Integer age = resolveAge(p, mr);
            if (age == null) continue;

            if (age <= 18) {
                ChildAlertDTO child = personMapper.toChildAlertDto(p);
                child.setAge(age);
                children.add(child);
            } else {
                AdultDTO adult = personMapper.toAdultDto(p);
                adults.add(adult);
            }
        }

         logger.debug("ChildAlert: {} children and {} adults found for address '{}'",
                 children.size(), adults.size(), targetAddress);

        if (children.isEmpty()) {
            logger.debug("ChildAlert: no children found for address '{}'", targetAddress);
            return List.of();
        }

        for(ChildAlertDTO child : children) {
            child.setHouseholdMembers(new ArrayList<>(adults));
        }

        return children;
    }

// --------------------- HELPERS -------------------------------
    /**
     * Normalise une valeur textuelle pour comparaison.
     *
     * <p>Règles : {@code trim()} puis {@code toLowerCase()}.</p>
     *
     * @param value texte à normaliser
     * @return texte normalisé
     */
    private String normalize(String value) {
        return value.trim().toLowerCase();
    }

    /**
     * Construit une clé d'index stable pour associer {@link Person} et {@link MedicalRecord}.
     *
     * <p>La clé est insensible à la casse et au padding via normalisation.</p>
     *
     * @param firstName prénom (peut être null)
     * @param lastName nom (peut être null)
     * @return clé sous la forme {@code firstname|lastname}
     */
    private String personKey (String firstName, String lastName) {
        return (firstName == null ? "" : normalize(firstName))
                + "|"
                + (lastName == null ? "" : normalize(lastName));
    }

    private Map<String, MedicalRecord> buildMedicalIndex() {

        Map<String, MedicalRecord> index = new HashMap<>();

        for (MedicalRecord mr : DataLoader.DATASOURCE.getMedicalrecords()) {
            index.put(personKey(mr.getFirstName(), mr.getLastName()), mr);
        }

        logger.debug("ChildAlert: medical index built with {} records", index.size());

        return index;
    }

    private List<Person> findHouseholdByAddress(String targetAddress) {
        List<Person> household = new ArrayList<>();

        for (Person p : DataLoader.DATASOURCE.getPersons()) {
            if(p.getAddress() == null) continue;
            if(normalize(p.getAddress()).equals(targetAddress)) {
                household.add(p);
            }
        }
        return household;
    }

    /**
     * Calcul l'âge d'une personne à partir de son dossier médical.
     *
     * <p>Retourne {@code null} si la date de naissance est absente/blanche ou invalide.</p>
     *
     * @param p personne concernée
     * @param mr dossier médical (peut être null)
     * @return âge en années, ou {@code null} si non calculable
     */
    private Integer resolveAge(Person p, MedicalRecord mr) {
        if (mr == null || mr.getBirthdate() == null || mr.getBirthdate().isBlank()) {
            logger.debug("ChildAlert : no birthdate for {} {}", p.getFirstName(), p.getLastName());
            return null;
        }
        try {
            return AgeUtils.calculateAge(mr.getBirthdate());
        } catch (IllegalArgumentException e) {
            logger.error("ChildAlert: invalid birthdate '{}' for {} {}",
                    mr.getBirthdate(), p.getFirstName(), p.getLastName(), e);
            return null;
        }
    }
}
