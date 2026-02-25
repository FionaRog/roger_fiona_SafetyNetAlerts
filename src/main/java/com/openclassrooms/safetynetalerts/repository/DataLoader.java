package com.openclassrooms.safetynetalerts.repository;

import com.openclassrooms.safetynetalerts.dto.SafetyNetDataDTO;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.io.File;

@Service
public class DataLoader {

    //singleton
    public static SafetyNetDataDTO DATASOURCE;

    @Bean
    public CommandLineRunner runner() {
        return args -> loadData();
    }

    public void loadData () {

        ObjectMapper objectMapper = new ObjectMapper();

        File file = new File("src/main/resources/datas.json");

        try {
            DATASOURCE = objectMapper.readValue(file, new TypeReference<SafetyNetDataDTO>() {
            });

            DATASOURCE.getPersons().forEach(System.out::println);
            DATASOURCE.getFirestations().forEach(System.out::println);
            DATASOURCE.getMedicalrecords().forEach(System.out::println);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
