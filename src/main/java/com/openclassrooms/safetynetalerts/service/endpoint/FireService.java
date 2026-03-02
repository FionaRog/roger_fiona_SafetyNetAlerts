package com.openclassrooms.safetynetalerts.service.endpoint;

import com.openclassrooms.safetynetalerts.dto.FirePersonDTO;
import com.openclassrooms.safetynetalerts.dto.FireResponseDTO;
import com.openclassrooms.safetynetalerts.mapper.FireMapper;
import com.openclassrooms.safetynetalerts.model.Firestation;
import com.openclassrooms.safetynetalerts.model.MedicalRecord;
import com.openclassrooms.safetynetalerts.model.Person;
import com.openclassrooms.safetynetalerts.repository.DataLoader;
import com.openclassrooms.safetynetalerts.utils.AgeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FireService implements IFireService {

    private final FireMapper fireMapper;

    private static final Logger logger = LoggerFactory.getLogger(FireService.class);

    public FireService (FireMapper fireMapper) {
        this.fireMapper = fireMapper;
    }


    public FireResponseDTO getPersonByAddress(String address) {
        logger.debug("Fire request: address='{}'", address);

        if(address == null || address.isBlank()) {
            logger.debug("Fire rejected : address is null/blank");
            return new FireResponseDTO();
        }

        String stationNumber = findStationByAddress(address);
          if(stationNumber == null) {
            logger.debug("Fire rejected : no station found for {}", address);
            return new FireResponseDTO();
        }
        logger.debug("Fire: station '{}' found at address {}", stationNumber, address);

        List<Person> personsAtAddress = findPersonsByAddress(address);
        logger.debug("Fire: {} person(s) found at address='{}'", personsAtAddress.size(), address);

        if (personsAtAddress.isEmpty()) {
            FireResponseDTO dto = new FireResponseDTO();
            dto.setStation(stationNumber);
            dto.setFirePersonDtos(List.of());
            logger.debug("Fire : No persons found for the address {}", address);
            return dto;
        }

        List<FirePersonDTO> firePersonDtos = buildFirePersonDtos(personsAtAddress);
        logger.debug("Fire: {} DTO(s) built for address='{}' (station='{}')",
                firePersonDtos.size(), address, stationNumber);


        FireResponseDTO response = new FireResponseDTO();
        response.setStation(stationNumber);
        response.setFirePersonDtos(firePersonDtos);
        return response;
    }



// ----------------------- HELPERS -------------------------------

    private String findStationByAddress(String address) {
        for (Firestation fs: DataLoader.DATASOURCE.getFirestations()) {
            if(fs == null || fs.getAddress() == null) continue;

            if(fs.getAddress().equalsIgnoreCase(address)) {
                return fs.getStation();
            }
        }
        return null;
    }

    private List<Person> findPersonsByAddress(String address) {
        List<Person> result = new ArrayList<>();

        for(Person p: DataLoader.DATASOURCE.getPersons()) {
            if(p == null || p.getAddress()== null) continue;

            if(p.getAddress().equalsIgnoreCase(address)) {
                result.add(p);
            }
        }
        return result;
    }

    private List<FirePersonDTO> buildFirePersonDtos(List<Person> personsAtAddress) {
        List<FirePersonDTO> dtos = new ArrayList<>();

        for (Person p : personsAtAddress) {
            MedicalRecord mr = findMedicalRecordByName(p.getFirstName(), p.getLastName());

            int age = 0;
            if (mr != null && mr.getBirthdate() != null && !mr.getBirthdate().isBlank()) {
                try {
                    age= AgeUtils.calculateAge(mr.getBirthdate());
                } catch (Exception e) {
                    logger.error("Fire: cannot parse birthdate='{}' for {} {}",
                            mr.getBirthdate(), p.getFirstName(), p.getLastName(), e);
                    age = 0;
                }
            }

            FirePersonDTO dto = fireMapper.toFirePersonDTO(p, mr, age);
            if (dto != null) {
                dtos.add(dto);
            }
        }
        return dtos;
    }

    private MedicalRecord findMedicalRecordByName(String firstName, String lastName) {
        if (firstName == null || lastName == null) {
            return  null;
        }

        for(MedicalRecord mr : DataLoader.DATASOURCE.getMedicalrecords()) {
            if(mr == null) continue;;
            if(mr.getFirstName() != null && mr.getLastName() != null
                && mr.getFirstName().equalsIgnoreCase(firstName)
                && mr.getLastName().equalsIgnoreCase(lastName)) {
                return mr;
            }
        }
        return null;
    }
}
