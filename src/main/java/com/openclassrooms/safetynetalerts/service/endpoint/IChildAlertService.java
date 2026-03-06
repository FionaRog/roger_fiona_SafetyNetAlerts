package com.openclassrooms.safetynetalerts.service.endpoint;

import com.openclassrooms.safetynetalerts.dto.ChildAlertDTO;

import java.util.List;

/**
 * Contrat du service permettant de récupérer les informations d'enfants
 * pour un foyer identifié par son adresse.
 *
 * <p>Le résultat est une liste d'enfants (inférieur ou égal à 18 ans) vivant à l'adresse donnée.
 * Chaque enfant inclut son âge et la liste des membres adultes du foyer.</p>
 *
 * <p>Si l'adresse est invalide (null/blanche) ou si aucun enfant n'est trouvé,
 * une liste vide est retournée.</p>
 *
 * @since 1.0
 */
public interface IChildAlertService {

    /**
     * Retourne la liste des enfants résidant à l'adresse fournie.
     *
     * <p>Règles :</p>
     * <ul>
     *   <li>Les enfants sont définis comme ayant un âge {@code <= 18}.</li>
     *   <li>Les adultes ({@code > 18}) sont ajoutés comme membres du foyer sur chaque enfant retourné.</li>
     * </ul>
     *
     * @param address adresse recherchée (peut être null ou blanche)
     * @return liste des enfants trouvés, ou liste vide si aucun résultat exploitable
     * @since 1.0
     */
    List<ChildAlertDTO> getChildByAddress(String address);
}
