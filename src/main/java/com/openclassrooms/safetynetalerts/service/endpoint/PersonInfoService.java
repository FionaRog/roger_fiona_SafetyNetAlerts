package com.openclassrooms.safetynetalerts.service.endpoint;

import com.openclassrooms.safetynetalerts.dto.PersonInfoDTO;
import com.openclassrooms.safetynetalerts.mapper.PersonInfoMapper;
import com.openclassrooms.safetynetalerts.model.MedicalRecord;
import com.openclassrooms.safetynetalerts.model.Person;
import com.openclassrooms.safetynetalerts.repository.DataLoader;
import com.openclassrooms.safetynetalerts.utils.AgeUtils;
import com.openclassrooms.safetynetalerts.utils.StringNormalizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Implémentation du service métier pour l'endpoint {@code /personInfo}.
 *
 * <p>Fonctionnement :</p>
 * <ul>
 *   <li>valide le nom de famille,</li>
 *   <li>recherche les personnes correspondant au nom fourni,</li>
 *   <li>construit un index des dossiers médicaux,</li>
 *   <li>pour chaque personne, tente de résoudre l'âge et construit un {@link PersonInfoDTO}
 *       via {@link PersonInfoMapper}.</li>
 * </ul>
 *
 *
 * <p>Si l'âge ne peut pas être calculé, il est renseigné à {@code 0}.</p>
 *
 * @since 1.0
 */
@Service
public class PersonInfoService implements IPersonInfoService {

    private static final Logger logger = LoggerFactory.getLogger(PersonInfoService.class);

    private final PersonInfoMapper personInfoMapper;

    /**
     * Construit le service PersonInfo.
     *
     * @param personInfoMapper mapper MapStruct utilisé pour construire les DTO
     * @since 1.0
     */
    public PersonInfoService(PersonInfoMapper personInfoMapper) {
        this.personInfoMapper = personInfoMapper;
    }

    /**
     * Retourne la liste des informations des personnes correspondant au nom fourni.
     *
     * <p>Si {@code lastName} est null/blanc ou si aucune personne ne correspond,
     * retourne une liste vide.</p>
     *
     * @param lastName nom de famille recherché
     * @return liste de {@link PersonInfoDTO} (peut être vide)
     * @since 1.0
     */
    public List<PersonInfoDTO> getPersonsByLastName(String lastName) {
        logger.debug("PersonInfo request: lastName={}", lastName);

        List<Person> matchedPersons = findPersonsByLastName(lastName);
        logger.debug("PersonInfo: {} persons found for lastName {}", matchedPersons.size(), lastName);

        if (matchedPersons.isEmpty()) {
            logger.debug("PersonInfo : no persons found for lastName {}", lastName);
            return List.of();
        }

        Map<String, MedicalRecord> medicalIndex = buildMedicalIndex();
        logger.debug("Person Info: medical index built with {} records", medicalIndex.size());


        List<PersonInfoDTO> personInfos = new ArrayList<>();

        for (Person p : matchedPersons) {
            if (p == null) continue;

            MedicalRecord mr = medicalIndex.get(personKey(p.getFirstName(), p.getLastName()));
            int age = resolveAge(p, mr);

            PersonInfoDTO dto = personInfoMapper.toPersonInfoDto(p, mr, age);
            if (dto != null) {
                personInfos.add(dto);
            }
        }
        logger.debug("PersonInfo : response built with {} persons", personInfos.size());
        return personInfos;
    }

// ------------------------ HELPERS ---------------------------------

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
     * Recherche les personnes correspondant au nom de famille fourni.
     *
     * <p>La comparaison est effectuée après normalisation (trim + lowercase).</p>
     *
     * <p>Si {@code lastName} est null/blanc, retourne une liste vide.</p>
     *
     * @param lastName nom de famille recherché
     * @return liste des {@link Person} correspondantes (peut être vide)
     */
    private List<Person> findPersonsByLastName(String lastName) {
        if (lastName == null || lastName.isBlank()) {
            logger.debug("PersonInfo rejected: lastName is null/blank");
            return List.of();
        }

        List<Person> result = new ArrayList<>();

        for (Person p : DataLoader.DATASOURCE.getPersons()) {
            if (p == null || p.getLastName() == null) continue;

            if (StringNormalizer.same(p.getLastName(), lastName)) {
                result.add(p);
            }
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
            logger.error("PersonInfo: cannot parse birthdate='{}' for {} {}",
                    mr.getBirthdate(), p.getFirstName(), p.getLastName(), e);
            return 0;
        }
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