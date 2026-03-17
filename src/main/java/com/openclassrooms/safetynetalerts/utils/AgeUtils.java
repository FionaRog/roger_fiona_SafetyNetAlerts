package com.openclassrooms.safetynetalerts.utils;

import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Classe utilitaire permettant de calculer l'âge d'une personne
 * à partir de sa date de naissance.
 *
 * <p>Le format attendu pour la date est :</p>
 * <ul>
 *   <li>{@code MM/dd/yyyy}</li>
 * </ul>
 *
 * <p>Exemple : {@code 03/12/1990}.</p>
 *
 * <p>Cette classe est utilitaire et ne doit pas être instanciée.</p>
 *
 * @since 1.0
 */
@NoArgsConstructor
public final class AgeUtils {

    /**
     * Formateur utilisé pour parser les dates de naissance.
     */
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("MM/dd/yyyy");

    /**
     * Calcule l'âge à partir d'une date de naissance.
     *
     * <p>La date doit respecter le format {@code MM/dd/yyyy}.</p>
     *
     * @param birthDate date de naissance sous forme de chaîne
     * @return âge calculé en années
     * @throws IllegalArgumentException si la date est null, vide
     *                                  ou si le format est invalide
     * @since 1.0
     */
    public static int calculateAge(String birthDate) {
        if (birthDate == null || birthDate.isBlank()) {
            throw new IllegalArgumentException("birthdate is null/blank");
        }

        try {
            LocalDate birthFormatted = LocalDate.parse(birthDate, FORMATTER);
            return Period.between(birthFormatted, LocalDate.now()).getYears();
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException(
                    "Invalid birthdate format (expected MM/dd/yyyy): " + birthDate, e);
        }
    }
}


