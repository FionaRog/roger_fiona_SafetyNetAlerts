package com.openclassrooms.safetynetalerts.utils;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public final class AgeUtils {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("MM/dd/yyyy");

    private AgeUtils() {

    }

    public static int calculateAge(String birthDate) {
        if (birthDate == null || birthDate.isBlank()) {
            throw new IllegalArgumentException("birthdate is null/blank");

        }

        try {
            LocalDate birthFormatted = LocalDate.parse(birthDate, FORMATTER);
            return Period.between(birthFormatted, LocalDate.now()).getYears();
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid birthdate format (expectedMM/dd/yyyy):" + birthDate, e);
        }
    }
}


