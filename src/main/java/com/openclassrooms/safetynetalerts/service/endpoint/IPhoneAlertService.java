package com.openclassrooms.safetynetalerts.service.endpoint;

import java.util.List;

/**
 * Contrat du service associé à l'endpoint {@code /phoneAlert}.
 *
 * <p>Retourne la liste des numéros de téléphone des personnes
 * couvertes par une station donnée.</p>
 *
 * <p>Si le paramètre est null/blanc ou si aucune correspondance n'est trouvée,
 * une liste vide est retournée.</p>
 *
 * @since 1.0
 */
public interface IPhoneAlertService {

    /**
     * Retourne les numéros de téléphone associés à la station fournie.
     *
     * @param firestation numéro de station (peut être null/blanc)
     * @return liste des numéros uniques (peut être vide)
     * @since 1.0
     */
    List<String> getPhoneByFirestation(String firestation);

}
