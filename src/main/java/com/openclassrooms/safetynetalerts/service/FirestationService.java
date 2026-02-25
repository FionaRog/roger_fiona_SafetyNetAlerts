package com.openclassrooms.safetynetalerts.service;

import com.openclassrooms.safetynetalerts.dto.FirestationPersonDTO;
import com.openclassrooms.safetynetalerts.dto.FirestationResponseDTO;
import com.openclassrooms.safetynetalerts.mapper.FirestationMapper;
import com.openclassrooms.safetynetalerts.model.Firestation;
import com.openclassrooms.safetynetalerts.model.MedicalRecord;
import com.openclassrooms.safetynetalerts.model.Person;
import com.openclassrooms.safetynetalerts.repository.DataLoader;
import com.openclassrooms.safetynetalerts.utils.AgeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class FirestationService implements IFirestationService{

    private static final Logger logger = LoggerFactory.getLogger(FirestationService.class);

    private final FirestationMapper firestationMapper;

    public FirestationService(FirestationMapper firestationMapper) {
        this.firestationMapper = firestationMapper;
    }

// --------------- CRUD ----------------------
    public List<Firestation> getFirestation() {

        return new ArrayList<>(DataLoader.DATASOURCE.getFirestations());
    }

    public boolean addFirestation(Firestation newfirestation) {

        if (newfirestation == null || newfirestation.getAddress() == null || newfirestation.getAddress().isBlank()) return false;
        if(newfirestation.getStation() == null || newfirestation.getStation().isBlank()) return false;

        for(Firestation firestation : DataLoader.DATASOURCE.getFirestations()) {
            if (normalize(firestation.getAddress()).equals(normalize(newfirestation.getAddress()))) {
                logger.debug("Add firestation rejected : duplicate for address {} ", firestation.getAddress());
                return false;
            }
        }
        DataLoader.DATASOURCE.getFirestations().add(newfirestation);

        logger.debug("firestation added with address {} and station {}",
                newfirestation.getAddress(), newfirestation.getStation());
        return true;
    }

    public boolean updateFirestationByAddress(Firestation updatedFirestation) {

        if (updatedFirestation == null || updatedFirestation.getAddress() == null || updatedFirestation.getAddress().isBlank()) return false;
        if(updatedFirestation.getStation() == null || updatedFirestation.getStation().isBlank()) return false;

        for (Firestation firestation : DataLoader.DATASOURCE.getFirestations()) {
            if (normalize(firestation.getAddress()).equals(normalize(updatedFirestation.getAddress()))) {

                firestation.setStation(updatedFirestation.getStation());

                logger.debug("firestation updated for address {} with updated station {}",
                        updatedFirestation.getAddress(), updatedFirestation.getStation());
                return true;
            }
        }
        logger.debug("Update firestation rejected for address {}", updatedFirestation.getAddress());
        return false;
    }

    public boolean deleteFirestationByAddress(String deletedAddress) {

        if(deletedAddress == null || deletedAddress.isBlank()) {
            logger.debug("Delete firestation rejected : address is null/blank");
            return false;
        }

        int before = DataLoader.DATASOURCE.getFirestations().size();
        DataLoader.DATASOURCE.getFirestations()
                .removeIf(fs -> fs.getAddress() != null && normalize(fs.getAddress()).equals(normalize(deletedAddress)));

        int after = DataLoader.DATASOURCE.getFirestations().size();

        boolean deleted = after < before;

        if (deleted) {
            logger.debug("Firestation for address '{}' deleted", deletedAddress);
        } else {
            logger.debug("Delete firestation rejected: no mapping found for address '{}'", deletedAddress);
        }

        return deleted;
    }

    public boolean deleteFirestationByStation(String deletedStation) {

        if(deletedStation == null || deletedStation.isBlank()) {
            logger.debug("Delete firestation rejected, station is null/blank");
            return false;
        }

        int before = DataLoader.DATASOURCE.getFirestations().size();
        DataLoader.DATASOURCE.getFirestations().
                removeIf(fs ->
                        fs.getStation() != null && normalize(fs.getStation()).equals(normalize(deletedStation)));

        int after = DataLoader.DATASOURCE.getFirestations().size();

        boolean deleted = after < before;

        if (deleted) {
            logger.debug("Firestation for station '{}' deleted", deletedStation);
        } else {
            logger.debug("Delete firestation rejected: no mapping found for station '{}'", deletedStation);
        }

        return deleted;
    }

// ------------------- ENDPOINT ---------------------
    public FirestationResponseDTO getPersonsByFirestation (String stationNumber) {

        if (stationNumber == null || stationNumber.isBlank()) {
           return emptyResponse();
        }

        // Construction de la liste des adresses couvertes par la station
        Set<String> coveredAddresses = new HashSet<>();

        for(Firestation fs : DataLoader.DATASOURCE.getFirestations()) {
            if(fs.getStation() !=null && normalize(fs.getStation()).equals(normalize(stationNumber))) {
                if(fs.getAddress() !=null && !fs.getAddress().isBlank()) {
                    coveredAddresses.add(normalize(fs.getAddress()));
                }
            }
        }

        logger.debug("Found {} covered addresses for station {}", coveredAddresses.size(), stationNumber);

        if( coveredAddresses.isEmpty()) {
            return emptyResponse();
        }

        // Créer un médical record pour les personnes
        Map<String, MedicalRecord> medicalIndex = buildMedicalIndex();
        logger.debug("Medical index built with {} entries", medicalIndex.size());

        // Initialiser la liste de réponse (people, adults, children)
        List<FirestationPersonDTO> people = new ArrayList<>();
        int adults = 0;
        int children = 0;

        //Ajout des personnes à la liste si correspondance avec adresse couverte
        for(Person person : DataLoader.DATASOURCE.getPersons()) {
            if(person.getAddress() == null) continue;

            String personAddress = normalize(person.getAddress());
            if(!coveredAddresses.contains(personAddress)) continue;

            // Mapping
            FirestationPersonDTO dto =
                    firestationMapper.toFirestationPersonDTO(person);
            people.add(dto);

            // Calcul de l'âge et classification des personnes
            Integer age = resolveAge(person, medicalIndex);
            if(age != null && age <= 18) {
                children ++;
            } else {
                adults ++;
            }
        }

        // Construction de la réponse
        FirestationResponseDTO response = new FirestationResponseDTO();
        response.setPeople(people);
        response.setAdults(adults);
        response.setChildren(children);

        return response;
    }

// ------------------ HELPERS -------------------

    private Map<String, MedicalRecord> buildMedicalIndex() {

        Map<String, MedicalRecord> medicalIndex = new HashMap<>();

        for(MedicalRecord mr : DataLoader.DATASOURCE.getMedicalrecords()) {
            String key = personKey(mr.getFirstName(), mr.getLastName());
            medicalIndex.put(key, mr);
        }
        return medicalIndex;
    }

    private Integer resolveAge(Person person, Map<String, MedicalRecord> medicalIndex) {
        String key = personKey(person.getFirstName(),person.getLastName());
        MedicalRecord mr = medicalIndex.get(key);

        if(mr == null || mr.getBirthdate() == null || mr.getBirthdate().isBlank()) {
            logger.debug("No medicalRecord/birthdate for {} {}",person.getFirstName(),person.getLastName());
            return null;
        }

        try {
            return AgeUtils.calculateAge(mr.getBirthdate());
        } catch (IllegalArgumentException e) {
            logger.error("Invalid birthdate '{}' for {} {}",
                    mr.getBirthdate(), person.getFirstName(), person.getLastName(), e);
            return null;
        }
    }

    private String normalize(String value) {
        return value == null ? "" : value.trim().toLowerCase();
    }

    private String personKey(String firstName, String lastName) {
        return normalize(firstName) + "|" + normalize(lastName);
    }


    private FirestationResponseDTO emptyResponse() {
        FirestationResponseDTO dto = new FirestationResponseDTO();
        dto.setPeople(List.of());
        dto.setAdults(0);
        dto.setChildren(0);
        return dto;
    }
}
