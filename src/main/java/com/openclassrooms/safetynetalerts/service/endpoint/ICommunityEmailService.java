package com.openclassrooms.safetynetalerts.service.endpoint;

import java.util.List;

public interface ICommunityEmailService {

    List<String> getEmailsByCity(String city);

}
