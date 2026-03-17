package com.openclassrooms.safetynetalerts.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entité représentant l'association entre une adresse
 * et une station de pompiers.
 *
 * <p>Utilisée pour déterminer quelle station couvre une adresse
 * dans plusieurs endpoints de l'API, notamment :</p>
 * <ul>
 *   <li>{@code GET /firestation}</li>
 *   <li>{@code GET /fire}</li>
 *   <li>{@code GET /flood/stations}</li>
 *   <li>{@code GET /phoneAlert}</li>
 * </ul>
 *
 * @since 1.0
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Firestation {

    /**
     * Adresse couverte par la station.
     */
    private String address;

    /**
     * Numéro de la station de pompiers.
     */
    private String station;
}
