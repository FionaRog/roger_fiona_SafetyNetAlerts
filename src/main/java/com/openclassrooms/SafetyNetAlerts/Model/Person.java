package com.openclassrooms.SafetyNetAlerts.Model;

import lombok.Data;


@Data
// @Entity et @Table(name = "persons") inutile car pas de lecture de base de données
public class Person {
    // Pas besoin de @column car nomenclature identique
    private String firstName;

    private String lastName;

    private String address;

    private String city;

    private String zip;

    private String phone;

    private String email;


}
