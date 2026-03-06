package com.openclassrooms.safetynetalerts.service.endpoint;

import com.openclassrooms.safetynetalerts.dto.FirePersonDTO;
import com.openclassrooms.safetynetalerts.dto.FireResponseDTO;
import com.openclassrooms.safetynetalerts.mapper.FireMapper;
import com.openclassrooms.safetynetalerts.model.Firestation;
import com.openclassrooms.safetynetalerts.model.MedicalRecord;
import com.openclassrooms.safetynetalerts.model.Person;
import com.openclassrooms.safetynetalerts.repository.DataLoader;
import com.openclassrooms.safetynetalerts.utils.AgeUtils;
import com.openclassrooms.safetynetalerts.utils.StringNormalizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Implémentation du service métier associé à l'endpoint {@code /fire}.
 *
 * <p>Fonctionnement :</p>
 * <ul>
 *   <li>valide l'adresse (null/blanche → DTO vide),</li>
 *   <li>recherche la station associée à l'adresse,</li>
 *   <li>recherche les personnes vivant à l'adresse,</li>
 *   <li>construit une liste de {@link FirePersonDTO} en s'appuyant sur {@link FireMapper}
 *       et sur les dossiers médicaux si disponibles.</li>
 * </ul>
 *
 *
 * <p>Comportements importants :</p>
 * <ul>
 *   <li>si aucune station n'est trouvée pour l'adresse : retourne un {@link FireResponseDTO} vide,</li>
 *   <li>si la station existe mais qu'aucune personne n'est trouvée :
 *       retourne un DTO avec la station renseignée et une liste vide,</li>
 *   <li>si la date de naissance est absente/invalide, l'âge est renseigné à {@code 0}.</li>
 * </ul>
 *
 * @since 1.0
 */
@Service
public class FireService implements IFireService {

    private final FireMapper fireMapper;

    private static final Logger logger = LoggerFactory.getLogger(FireService.class);

    /**
     * Construit le service Fire.
     *
     * @param fireMapper mapper MapStruct utilisé pour construire les DTO
     * @since 1.0
     */
    public FireService(FireMapper fireMapper) {
        this.fireMapper = fireMapper;
    }

    /**
     * Retourne la station et les informations des personnes vivant à l'adresse fournie.
     *
     * @param address adresse recherchée (peut être null ou blanche)
     * @return {@link FireResponseDTO} contenant la station et la liste des {@link FirePersonDTO}
     * @since 1.0
     */
    public FireResponseDTO getPersonByAddress(String address) {
        logger.debug("Fire request: address='{}'", address);

        if (address == null || address.isBlank()) {
            logger.debug("Fire rejected : address is null/blank");
            return new FireResponseDTO();
        }

        String targetAddress = StringNormalizer.norm(address);

        String stationNumber = findStationByAddress(targetAddress);

        if (stationNumber == null) {
            logger.debug("Fire rejected : no station found for {}", address);
            return new FireResponseDTO();
        }
        logger.debug("Fire: station '{}' found at address {}", stationNumber, address);

        List<Person> personsAtAddress = findPersonsByAddress(targetAddress);
        logger.debug("Fire: {} person(s) found at address='{}'", personsAtAddress.size(), address);

        if (personsAtAddress.isEmpty()) {
            FireResponseDTO dto = new FireResponseDTO();
            dto.setStation(stationNumber);
            dto.setFirePersonDtos(List.of());
            logger.debug("Fire : No persons found for the address {}", address);
            return dto;
        }

        List<FirePersonDTO> firePersonDtos = buildFirePersonDtos(personsAtAddress);
        logger.debug("Fire: {} DTO(s) built for address='{}' (station='{}')",
                firePersonDtos.size(), address, stationNumber);


        FireResponseDTO response = new FireResponseDTO();
        response.setStation(stationNumber);
        response.setFirePersonDtos(firePersonDtos);
        return response;
    }


// ----------------------- HELPERS -------------------------------

    /**
     * Recherche le numéro de station couvrant l'adresse donnée.
     *
     * <p>La comparaison d'adresse est réalisée après normalisation (trim + lowercase).</p>
     *
     * @param address adresse recherchée
     * @return numéro de station, ou {@code null} si aucune correspondance
     */
    private String findStationByAddress(String address) {
        for (Firestation fs : DataLoader.DATASOURCE.getFirestations()) {
            if (fs == null || fs.getAddress() == null) continue;

            if (StringNormalizer.same(fs.getAddress(), address)) {
                return fs.getStation();
            }
        }
        return null;
    }

    /**
     * Recherche les personnes vivant à l'adresse donnée.
     *
     * <p>La comparaison est réalisée après normalisation (trim + lowercase).</p>
     *
     * <p>Les entrées null sont ignorées. La liste retournée
     * n'est jamais {@code null}.</p>
     *
     * @param address adresse recherchée
     * @return liste des personnes trouvées (peut être vide)
     */
    private List<Person> findPersonsByAddress(String address) {
        List<Person> result = new ArrayList<>();

        for (Person p : DataLoader.DATASOURCE.getPersons()) {
            if (p == null || p.getAddress() == null) continue;

            if (StringNormalizer.same(p.getAddress(), address)) {
                result.add(p);
            }
        }
        return result;
    }

    /**
     * Construit la liste des {@link FirePersonDTO} à partir des personnes présentes à l'adresse.
     *
     * <p>Pour chaque personne, tente de récupérer le {@link MedicalRecord} associé et d'en déduire l'âge.
     * En cas de date invalide, l'âge est fixé à {@code 0}.</p>
     *
     * @param personsAtAddress personnes vivant à l'adresse
     * @return liste des DTO construits (jamais null)
     */
    private List<FirePersonDTO> buildFirePersonDtos(List<Person> personsAtAddress) {
        List<FirePersonDTO> dtos = new ArrayList<>();

        for (Person p : personsAtAddress) {
            MedicalRecord mr = findMedicalRecordByName(p.getFirstName(), p.getLastName());

            int age = 0;
            if (mr != null && mr.getBirthdate() != null && !mr.getBirthdate().isBlank()) {
                try {
                    age = AgeUtils.calculateAge(mr.getBirthdate());
                } catch (Exception e) {
                    logger.error("Fire: cannot parse birthdate='{}' for {} {}",
                            mr.getBirthdate(), p.getFirstName(), p.getLastName(), e);
                    age = 0;
                }
            }

            FirePersonDTO dto = fireMapper.toFirePersonDTO(p, mr, age);
            if (dto != null) {
                dtos.add(dto);
            }
        }
        return dtos;
    }

    /**
     * Recherche le dossier médical correspondant au prénom et nom fournis.
     *
     * <p>La comparaison est réalisée après normalisation (trim + lowercase).</p>
     *
     * <p>Retourne {@code null} si aucun dossier n'est trouvé
     * ou si les paramètres sont null.</p>
     *
     * @param firstName prénom recherché
     * @param lastName  nom recherché
     * @return {@link MedicalRecord} correspondant, ou {@code null} si absent
     */
    private MedicalRecord findMedicalRecordByName(String firstName, String lastName) {
        if (firstName == null || lastName == null) {
            return null;
        }

        for (MedicalRecord mr : DataLoader.DATASOURCE.getMedicalrecords()) {
            if (mr == null) continue;

            if (StringNormalizer.same(mr.getFirstName(), firstName)
                    && StringNormalizer.same(mr.getLastName(), lastName)) {
                return mr;
            }
        }
        return null;
    }
}
