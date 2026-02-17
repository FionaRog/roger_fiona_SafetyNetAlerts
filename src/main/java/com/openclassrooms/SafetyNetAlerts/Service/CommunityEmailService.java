package com.openclassrooms.SafetyNetAlerts.Service;

import com.openclassrooms.SafetyNetAlerts.Model.Person;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
//contient la logique métier de l'endpoint
//Parcourt toutes les personnes, garde celles de la ville demandée, récupère leurs emails, enlève les doublons, puis renvoie la liste.
public class CommunityEmailService {

    public List<String> getEmailsByCity(String city) {
        //Si city est null ou vide, on renvoie une liste vide (réponse JSON vide)
        if (city == null || city.isBlank()) {
            return List.of();
        }

        //Set pour éviter les doublons d'emails
        Set<String> uniqueEmails = new HashSet<>();

        //On parcourt toutes les personnes de la source de données
        for (Person person : DataRunner.DATASOURCE.getPersons()) {

            //On vérifie que la ville de la personne n'est pas null
            //et qu'elle correspond à la ville demandée (sans tenir compte des majuscules ou minuscules)
            if (person.getCity() != null && person.getCity().equalsIgnoreCase(city)) {

                // 5) On ajoute l'email si non null / non vide
                if (person.getEmail() != null && !person.getEmail().isBlank()) {
                    uniqueEmails.add(person.getEmail());
                }
            }
        }
        //On convertit le Set en List pour retourner un JSON de type tableau
        return new ArrayList<>(uniqueEmails);
    }
}
