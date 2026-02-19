package com.openclassrooms.SafetyNetAlerts.Service;

import com.openclassrooms.SafetyNetAlerts.Model.Firestation;
import org.springframework.stereotype.Service;

@Service
public class FirestationCrudService {

    public boolean addMapping (Firestation newfirestation) {

        for(Firestation firestation : DataRunner.DATASOURCE.getFirestations()) {
            if (firestation.getAddress().equalsIgnoreCase(newfirestation.getAddress()))
                return false;
        }
        DataRunner.DATASOURCE.getFirestations().add(newfirestation);
        return true;
    }

    public boolean updateStationByAddress(Firestation updatedFirestation) {

        for (Firestation firestation : DataRunner.DATASOURCE.getFirestations()) {
            if (firestation.getAddress().equalsIgnoreCase(updatedFirestation.getAddress())) {

                firestation.setStation(updatedFirestation.getStation());

                return true;
            }
        }
        return false;
    }

    //Changer pour removeIf
    public boolean deleteByAddress(String deletedAddress) {

        for (Firestation firestation : DataRunner.DATASOURCE.getFirestations()) {
            if(firestation.getAddress().equalsIgnoreCase(deletedAddress)) {

                DataRunner.DATASOURCE.getFirestations().remove(firestation);

                return true;
            }
        }
        return false;
    }

    public boolean deleteByStation(String deletedStation) {

        int before = DataRunner.DATASOURCE.getFirestations().size();

        DataRunner.DATASOURCE.getFirestations().removeIf(fs ->
                fs.getStation() != null && fs.getStation().equals(deletedStation));

        int after = DataRunner.DATASOURCE.getFirestations().size();

        return after < before;

    }
}

