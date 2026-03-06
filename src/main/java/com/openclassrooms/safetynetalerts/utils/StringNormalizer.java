package com.openclassrooms.safetynetalerts.utils;

/**
 * Classe utilitaire centralisant la normalisation des chaînes de caractères
 * pour les comparaisons dans l'application.
 *
 * <p>Règle de normalisation : {@code trim()} puis {@code toLowerCase()}.
 * Si la valeur est {@code null}, une chaîne vide est retournée.</p>
 *
 * @since 1.0
 */
public final class StringNormalizer {

    /**
     * Constructeur privé pour empêcher l'instanciation de la classe utilitaire.
     */
    private StringNormalizer() {
    }

    /**
     * Normalise une chaîne pour comparaison.
     *
     * @param value valeur à normaliser
     * @return valeur normalisée (jamais {@code null})
     * @since 1.0
     */
    public static String norm(String value) {
        return value == null ? "" : value.trim().toLowerCase();
    }

    /**
     * Indique si deux chaînes sont égales après normalisation.
     *
     * @param a première valeur
     * @param b seconde valeur
     * @return {@code true} si égales après normalisation, {@code false} sinon
     * @since 1.0
     */
    public static boolean same(String a, String b) {
        return norm(a).equals(norm(b));
    }
}