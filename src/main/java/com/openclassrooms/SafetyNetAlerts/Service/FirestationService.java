package com.openclassrooms.SafetyNetAlerts.Service;

import com.openclassrooms.SafetyNetAlerts.Dto.FirestationPersonDTO;
import com.openclassrooms.SafetyNetAlerts.Dto.FirestationResponseDTO;
import com.openclassrooms.SafetyNetAlerts.Model.Firestation;
import com.openclassrooms.SafetyNetAlerts.Model.MedicalRecord;
import com.openclassrooms.SafetyNetAlerts.Model.Person;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.*;


@Service
public class FirestationService {

    private static final Logger logger = LoggerFactory.getLogger(FirestationService.class);

    private static final DateTimeFormatter BIRTHDATE_FORMATTER = DateTimeFormatter.ofPattern("MM/dd/yyyy");

    //Récupération a
    public FirestationResponseDTO getPersonsByFirestation (String firestation) {

        //Si paramètre vide , réponse vide
        if (firestation == null || firestation.isBlank()) {
           return emptyResponse();
        }

        // Récupération des adresses correspondant à une station
        Set<String> coveredAddress = new HashSet<>();

        for(Firestation fs : DataRunner.DATASOURCE.getFirestations()) {
            if(fs.getStation() !=null && fs.getStation().trim().equals(firestation)) {
                if(fs.getAddress() !=null && !fs.getAddress().isBlank()) {
                    coveredAddress.add(fs.getAddress().trim().toLowerCase());
                }
            }
        }
        //Si adresse vide , réponse vide
        if( coveredAddress.isEmpty()) {
            return emptyResponse();
        }

        //
        Map<String, MedicalRecord> medicalIndex = new HashMap<>();
        for(MedicalRecord mr : DataRunner.DATASOURCE.getMedicalrecords()) {
            String key = personKey(mr.getFirstName(), mr.getLastName());
            medicalIndex.put(key, mr);
        }

        //On parcourt les personnes, on garde celles dont l'adresse est couverte, puis :
        //    - on construit le DTO "personne"
        //    - on calcule l'âge via MedicalRecord
        //    - on incrémente adults/children
        List<FirestationPersonDTO> people = new ArrayList<>();
        int adults = 0;
        int children = 0;

        for(Person person : DataRunner.DATASOURCE.getPersons()) {
            if(person.getAddress() == null) continue;

            String personAddress = normalize(person.getAddress());
            if(!coveredAddress.contains(personAddress)) continue;

            FirestationPersonDTO dto = new FirestationPersonDTO();
            dto.setFirstName(person.getFirstName());
            dto.setLastName(person.getLastName());
            dto.setAddress(person.getAddress());
            dto.setPhone(person.getPhone());

            people.add(dto);

            //Calcul d'âge
            String key = personKey(person.getFirstName(),person.getLastName());
            MedicalRecord mr = medicalIndex.get(key);

            Integer age = null;
            if (mr != null && mr.getBirthdate() !=null && !mr.getBirthdate().isBlank()) {
                try {
                    age = calculatedAge(mr.getBirthdate());
                } catch (Exception e) {
                    logger.error("Invalid birthdate '{}' for {} {}", mr.getBirthdate(),person.getFirstName(), person.getLastName(), e);
                }
            } else {
                logger.debug("No medicalRecord found for {} {}", person.getFirstName(), person.getLastName());
            }
            if(age != null && age <= 18) {
                children ++;
            } else {
                adults ++;
            }
        }

        FirestationResponseDTO response = new FirestationResponseDTO();
        response.setPeople(people);
        response.setAdults(adults);
        response.setChildren(children);

        return response;
    }

    private String normalize(String value) {
        return value.trim().toLowerCase();
    }

    private String personKey(String firstName, String lastName) {
        return (firstName == null ? "" : firstName.trim().toLowerCase())
                + "|"
                + (lastName == null ? "" : lastName.trim().toLowerCase());
    }

    //Calcul de l'âge des personnes
    private int calculatedAge(String birthdate) {
        LocalDate birth = LocalDate.parse(birthdate, BIRTHDATE_FORMATTER);
        return Period.between(birth, LocalDate.now()).getYears();
    }

    private FirestationResponseDTO emptyResponse() {
        FirestationResponseDTO dto = new FirestationResponseDTO();
        dto.setPeople(List.of());
        dto.setAdults(0);
        dto.setChildren(0);
        return dto;
    }
}
