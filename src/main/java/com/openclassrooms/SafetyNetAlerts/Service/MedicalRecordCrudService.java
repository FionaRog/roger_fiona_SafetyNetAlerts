package com.openclassrooms.SafetyNetAlerts.Service;

import com.openclassrooms.SafetyNetAlerts.Model.MedicalRecord;
import org.springframework.stereotype.Service;

@Service
public class MedicalRecordCrudService {

    public boolean addMedicalRecord (MedicalRecord newMedicalRecord) {

        for(MedicalRecord medicalRecord : DataRunner.DATASOURCE.getMedicalrecords()) {
            if (medicalRecord.getFirstName().equalsIgnoreCase(newMedicalRecord.getFirstName())
                    && medicalRecord.getLastName().equalsIgnoreCase(newMedicalRecord.getLastName())) {
                return false;
            }
        }
        DataRunner.DATASOURCE.getMedicalrecords().add(newMedicalRecord);
        return true;

    }

    public boolean updateMedicalRecord (MedicalRecord updatedMedicalRecord) {

        for(MedicalRecord medicalRecord : DataRunner.DATASOURCE.getMedicalrecords()) {
            if(medicalRecord.getFirstName().equalsIgnoreCase(updatedMedicalRecord.getFirstName())
                    && medicalRecord.getLastName().equalsIgnoreCase(updatedMedicalRecord.getLastName())) {

                medicalRecord.setMedications(updatedMedicalRecord.getMedications());
                medicalRecord.setAllergies(updatedMedicalRecord.getAllergies());
                medicalRecord.setBirthdate(updatedMedicalRecord.getBirthdate());

                return true;
            }
        }
        return false;
    }

    //Changer pour removeIf
    public boolean deletedMedicalRecord (String firstName, String lastName) {

        for(MedicalRecord medicalRecord : DataRunner.DATASOURCE.getMedicalrecords()) {
            if(medicalRecord.getFirstName().equalsIgnoreCase(firstName)
                    && medicalRecord.getLastName().equalsIgnoreCase(lastName)) {

                DataRunner.DATASOURCE.getMedicalrecords().remove(medicalRecord);

                return true;
            }
        }
        return false;
    }
}
