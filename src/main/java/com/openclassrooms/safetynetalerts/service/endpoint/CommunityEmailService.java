package com.openclassrooms.safetynetalerts.service.endpoint;

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
 * Implémentation du service permettant de récupérer les adresses e-mail
 * des habitants d'une ville donnée.
 *
 * <p>Fonctionnement :</p>
 * <ul>
 *   <li>valide le paramètre d'entrée (null ou blanc → liste vide),</li>
 *   <li>parcourt les personnes chargées via {@link DataLoader},</li>
 *   <li>filtre celles dont la ville correspond après normalisation (trim + lowercase),,</li>
 *   <li>retourne les adresses e-mail uniques.</li>
 * </ul>
 *
 *
 * <p>Les e-mails null ou blancs sont ignorés.</p>
 *
 * @since 1.0
 */
@Service
public class CommunityEmailService implements ICommunityEmailService {

    private static final Logger logger = LoggerFactory.getLogger(CommunityEmailService.class);

    /**
     * Retourne la liste des adresses e-mail uniques pour la ville fournie.
     *
     * <p>La comparaison de la ville est réalisée après normalisation (trim + lowercase).</p>
     *
     * @param city nom de la ville recherchée
     * @return liste d'e-mails uniques, ou liste vide si aucun résultat
     * @since 1.0
     */
    public List<String> getEmailsByCity(String city) {
        logger.debug("CommunityEmail request: city='{}'", city);

        if (city == null || city.isBlank()) {
            logger.debug("CommunityEmail rejected: city is null/blank");
            return List.of();
        }

        Set<String> uniqueEmails = new HashSet<>();

        for (Person person : DataLoader.PERSONS) {

            if (person.getCity() != null && StringNormalizer.same(person.getCity(), city)) {

                if (person.getEmail() != null && !person.getEmail().isBlank()) {
                    uniqueEmails.add(person.getEmail());
                }
            }
        }
        logger.debug("CommunityEmail: {} emails returned for city '{}'",
                uniqueEmails.size(), city);

        return new ArrayList<>(uniqueEmails);
    }
}
