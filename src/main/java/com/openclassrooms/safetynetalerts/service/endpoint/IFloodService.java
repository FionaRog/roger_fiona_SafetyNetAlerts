package com.openclassrooms.safetynetalerts.service.endpoint;

import com.openclassrooms.safetynetalerts.dto.FloodHouseholdDTO;

import java.util.List;

/**
 * Contrat du service associé à l'endpoint {@code /flood/stations}.
 *
 * <p>Le service retourne une liste de foyers regroupés par adresse.
 * Chaque foyer contient les résidents et leurs informations médicales si disponibles.</p>
 *
 * <p>Si la liste de stations est null/vide, ou si aucune adresse n'est couverte,
 * une liste vide est retournée.</p>
 *
 * @since 1.0
 */
public interface IFloodService {

    /**
     * Retourne les foyers couverts par les stations fournies.
     *
     * @param stations liste de numéros de station (peut être null/vide)
     * @return liste de {@link FloodHouseholdDTO}, ou liste vide si aucun résultat
     * @since 1.0
     */
    List<FloodHouseholdDTO> getHouseholdsByStations(List<String> stations);

}
