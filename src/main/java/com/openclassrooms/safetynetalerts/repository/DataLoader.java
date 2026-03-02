package com.openclassrooms.safetynetalerts.repository;

import com.openclassrooms.safetynetalerts.dto.SafetyNetDataDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.io.InputStream;

@Service
public class DataLoader {

    //singleton
    public static SafetyNetDataDTO DATASOURCE;

    @Value("${data-path}")
    private String datasourceFilePath;

    @Bean
    public CommandLineRunner runner() {
        return args -> loadData();
    }

    public void loadData () {

        ObjectMapper objectMapper = new ObjectMapper();

        try (InputStream is = new ClassPathResource(datasourceFilePath).getInputStream()){
            DATASOURCE = objectMapper.readValue(is, new TypeReference<SafetyNetDataDTO>() {
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
