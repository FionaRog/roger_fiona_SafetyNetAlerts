package com.openclassrooms.safetynetalerts.service.endpoint;

import java.util.List;

/**
 * Contrat du service permettant de récupérer les adresses e-mail
 * des habitants d'une ville donnée.
 *
 * <p>Les adresses retournées sont uniques et correspondent
 * aux personnes dont la ville correspond (comparaison insensible à la casse).</p>
 *
 * <p>Si la ville est null ou blanche, une liste vide est retournée.</p>
 *
 * @since 1.0
 */
public interface ICommunityEmailService {

    /**
     * Retourne la liste des adresses e-mail uniques pour une ville donnée.
     *
     * @param city nom de la ville (peut être null ou blanc)
     * @return liste des e-mails uniques correspondant à la ville,
     *         ou liste vide si aucun résultat exploitable
     * @since 1.0
     */
    List<String> getEmailsByCity(String city);

}
