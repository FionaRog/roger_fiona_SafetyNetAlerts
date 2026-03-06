package com.openclassrooms.safetynetalerts.model;

import lombok.Getter;
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
public class Firestation {
    /**
     * Construit une association entre une adresse et une caserne de pompiers.
     *
     <p>Ce constructeur facilite l'initialisation d'un {@link Firestation}
     * lors de tests unitaires ou de créations programmatiques.</p>
     *
     *
     * @param address adresse couverte par la station
     * @param station numéro de la station de pompiers
     */
    public Firestation(String address, String station) {
        this.address = address;
        this.station = station;
    }

    /**
     * Adresse couverte par la station.
     */
    private String address;

    /**
     * Numéro de la station de pompiers.
     */
    private String station;
}
