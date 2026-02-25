package com.openclassrooms.safetynetalerts.service.endpoint;

import java.util.List;

public interface IPhoneAlertService {

    List<String> getPhoneByFirestation(String firestation);

}
