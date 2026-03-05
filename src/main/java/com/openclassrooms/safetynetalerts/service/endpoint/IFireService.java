package com.openclassrooms.safetynetalerts.service.endpoint;

import com.openclassrooms.safetynetalerts.dto.FireResponseDTO;

/**
 * Contrat du service permettant de fournir les informations de l'endpoint {@code /fire}.
 *
 * <p>Le service retourne un {@link FireResponseDTO} contenant :</p>
 * <ul>
 *   <li>le numéro de station couvrant l'adresse,</li>
 *   <li>la liste des personnes vivant à cette adresse, enrichie si possible avec leurs données médicales.</li>
 * </ul>
 *
 *
 * <p>Si l'adresse est invalide (null/blanche) ou si aucune station ne couvre l'adresse,
 * un {@link FireResponseDTO} vide est retourné.</p>
 *
 * @since 1.0
 */
public interface IFireService {

    /**
     * Retourne la station et les personnes vivant à l'adresse fournie.
     *
     * @param address adresse recherchée (peut être null ou blanche)
     * @return {@link FireResponseDTO} résultat (peut être vide)
     * @since 1.0
     */
    FireResponseDTO getPersonByAddress (String address);
}
