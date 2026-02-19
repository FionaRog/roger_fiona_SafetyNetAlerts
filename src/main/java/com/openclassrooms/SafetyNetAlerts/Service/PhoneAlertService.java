package com.openclassrooms.SafetyNetAlerts.Service;

import com.openclassrooms.SafetyNetAlerts.Model.Firestation;
import com.openclassrooms.SafetyNetAlerts.Model.Person;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Service
public class PhoneAlertService {

    public List<String> getPhoneByFirestation(String firestation) {

        //Récupération des adresses couvertes par la station
        if (firestation == null || firestation.isBlank()) {
            return List.of();
        }

        Set<String> coveredAdresses = new HashSet<>();

        for (Firestation fs : DataRunner.DATASOURCE.getFirestations()) {
            if (fs.getStation() !=null && fs.getStation().trim().equals(firestation)) {
                if(fs.getAddress() !=null && !fs.getAddress().isBlank()) {
                    coveredAdresses.add(normalize(fs.getAddress()));
                }
            }
        }

        if (coveredAdresses.isEmpty()) {
            return List.of();
        }

        //Récupération des numéros de téléphone vivant à ces adresses
        Set<String> uniquePhones = new HashSet<>();

        for(Person person : DataRunner.DATASOURCE.getPersons()) {
            if (person.getAddress() !=null) {
                String personAdress = normalize(person.getAddress());
                if (coveredAdresses.contains(personAdress)) {
                    if(person.getPhone() !=null && !person.getPhone().isBlank()) {
                        uniquePhones.add(person.getPhone().trim());
                    }
                }
            }
        }
        //Conversion en List pour retourner un tableau JSON
        return new ArrayList<>(uniquePhones);
    }
    private String normalize(String value) {
        return value.trim().toLowerCase();
    }
}
