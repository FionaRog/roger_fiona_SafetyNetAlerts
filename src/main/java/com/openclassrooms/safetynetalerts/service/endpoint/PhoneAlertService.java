package com.openclassrooms.safetynetalerts.service.endpoint;

import com.openclassrooms.safetynetalerts.model.Firestation;
import com.openclassrooms.safetynetalerts.model.Person;
import com.openclassrooms.safetynetalerts.repository.DataLoader;
import com.openclassrooms.safetynetalerts.utils.StringNormalizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Implémentation du service métier pour l'endpoint {@code /phoneAlert}.
 *
 * <p>Fonctionnement :</p>
 * <ul>
 *   <li>valide le numéro de station,</li>
 *   <li>détermine les adresses couvertes par la station,</li>
 *   <li>collecte les numéros de téléphone des personnes vivant à ces adresses,</li>
 *   <li>retourne une liste de numéros uniques.</li>
 * </ul>
 *
 *
 * <p>Les comparaisons sont effectuées après normalisation
 * (trim + lowercase pour les stations et adresses).</p>
 *
 * @since 1.0
 */
@Service
public class PhoneAlertService implements IPhoneAlertService {

    private static final Logger logger = LoggerFactory.getLogger(PhoneAlertService.class);

    /**
     * Retourne les numéros de téléphone associés à la station fournie.
     *
     * <p>Si {@code firestation} est null/blanc ou si aucune adresse n'est couverte,
     * retourne une liste vide.</p>
     *
     * @param firestation numéro de station recherché
     * @return liste des numéros uniques (peut être vide)
     * @since 1.0
     */
    public List<String> getPhoneByFirestation(String firestation) {
        logger.debug("PhoneAlert request: firestation='{}'", firestation);

        if (firestation == null || firestation.isBlank()) {
            logger.debug("PhoneAlert rejected: firestation is null/blank");
            return List.of();
        }

        Set<String> coveredAdresses = findCoveredAddressesByStation(firestation);
        logger.debug("PhoneAlert: {} addresses covered found for station '{}'",
                coveredAdresses.size(), firestation);

        if (coveredAdresses.isEmpty()) {
            logger.debug("PhoneAlert: no addresses found");
            return List.of();
        }

        Set<String> uniquePhones = collectPhonesByAddress(coveredAdresses);
        logger.debug("PhoneAlert: {} phone numbers collected for station '{}'", uniquePhones.size(), firestation);
        return new ArrayList<>(uniquePhones);
    }


    // ----------------------- HELPERS ---------------------------------

    /**
     * Construit l'ensemble des adresses couvertes par la station fournie.
     *
     * <p>La comparaison du numéro de station est effectuée après normalisation
     * (trim + lowercase).</p>
     *
     * <p>Les adresses retournées sont également normalisées.</p>
     *
     * @param firestation numéro de station recherché
     * @return ensemble des adresses couvertes (jamais {@code null})
     */
    private Set<String> findCoveredAddressesByStation(String firestation) {
        Set<String> coveredAdresses = new HashSet<>();

        for (Firestation fs : DataLoader.DATASOURCE.getFirestations()) {
            if (fs.getStation() != null && StringNormalizer.same(fs.getStation(), firestation)) {
                if (fs.getAddress() != null && !fs.getAddress().isBlank()) {
                    coveredAdresses.add(StringNormalizer.norm(fs.getAddress()));
                }
            }
        }
        return coveredAdresses;
    }

    /**
     * Collecte les numéros de téléphone des personnes vivant
     * aux adresses couvertes.
     *
     * <p>Les numéros null/blancs sont ignorés.
     * Les numéros sont retournés sous forme unique (Set).</p>
     *
     * @param coveredAdresses ensemble des adresses couvertes (normalisées)
     * @return ensemble des numéros uniques (jamais {@code null})
     */
    private Set<String> collectPhonesByAddress(Set<String> coveredAdresses) {
        Set<String> uniquePhones = new HashSet<>();

        for (Person person : DataLoader.DATASOURCE.getPersons()) {
            if (person.getAddress() != null) {

                String personAdress = StringNormalizer.norm(person.getAddress());

                if (coveredAdresses.contains(personAdress)) {
                    if (person.getPhone() != null && !person.getPhone().isBlank()) {
                        uniquePhones.add(person.getPhone().trim());
                    }
                }
            }
        }
        return uniquePhones;
    }
}