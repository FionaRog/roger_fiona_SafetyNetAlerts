package com.openclassrooms.safetynetalerts.service.crud;

import com.openclassrooms.safetynetalerts.model.MedicalRecord;
import com.openclassrooms.safetynetalerts.repository.DataLoader;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class MedicalRecordCrudService implements IMedicalRecordCrudService {

    private static final Logger logger = LoggerFactory.getLogger(MedicalRecordCrudService.class);

    public List<MedicalRecord> getMedicalRecord() {

        return new ArrayList<>(DataLoader.DATASOURCE.getMedicalrecords());
    }

    public boolean addMedicalRecord (MedicalRecord newMedicalRecord) {

        for(MedicalRecord medicalRecord : DataLoader.DATASOURCE.getMedicalrecords()) {
            if (medicalRecord.getFirstName().equalsIgnoreCase(newMedicalRecord.getFirstName())
                    && medicalRecord.getLastName().equalsIgnoreCase(newMedicalRecord.getLastName())) {
                logger.debug("add medicalRecord rejected: duplicate for {} {}",
                        newMedicalRecord.getFirstName(), newMedicalRecord.getLastName());
                return false;
            }
        }
        DataLoader.DATASOURCE.getMedicalrecords().add(newMedicalRecord);

        logger.debug("medicalRecord added for {} {}",
                newMedicalRecord.getFirstName(), newMedicalRecord.getLastName());
        return true;

    }

    public boolean updateMedicalRecord (MedicalRecord updatedMedicalRecord) {

        for(MedicalRecord medicalRecord : DataLoader.DATASOURCE.getMedicalrecords()) {
            if(medicalRecord.getFirstName().equalsIgnoreCase(updatedMedicalRecord.getFirstName())
                    && medicalRecord.getLastName().equalsIgnoreCase(updatedMedicalRecord.getLastName())) {

                medicalRecord.setMedications(updatedMedicalRecord.getMedications());
                medicalRecord.setAllergies(updatedMedicalRecord.getAllergies());
                medicalRecord.setBirthdate(updatedMedicalRecord.getBirthdate());

                logger.debug("medicalRecord updated for {} {}",
                        medicalRecord.getFirstName(), medicalRecord.getLastName());
                return true;
            }
        }
        logger.debug("update medicalRecord rejected for {} {}",
                updatedMedicalRecord.getFirstName(), updatedMedicalRecord.getLastName());
        return false;
    }

    public boolean deletedMedicalRecord (String firstName, String lastName) {

        if(firstName == null || firstName.isBlank() || lastName == null || lastName.isBlank()) {
            logger.debug("Delete medicalRecord rejected, firstName/lastName is null/blank");
            return false;
        }

        int before = DataLoader.DATASOURCE.getMedicalrecords().size();

        DataLoader.DATASOURCE.getMedicalrecords().removeIf(mr ->
                mr.getFirstName() != null
                && mr.getLastName() != null
                && mr.getFirstName().toLowerCase().equalsIgnoreCase(firstName)
                && mr.getLastName().toLowerCase().equalsIgnoreCase(lastName)
        );

        int after = DataLoader.DATASOURCE.getMedicalrecords().size();

        logger.debug("medicalRecord deleted for {} {}", firstName, lastName);
        return after < before;
    }
}
