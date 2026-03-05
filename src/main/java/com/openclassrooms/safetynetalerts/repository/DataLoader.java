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

/**
 * Composant Spring responsable du chargement des données
 * initiales de l'application SafetyNetAlerts.
 *
 * <p>Les données sont lues depuis un fichier JSON situé dans le classpath
 * et sont désérialisées en {@link SafetyNetDataDTO} à l'aide de Jackson.</p>
 *
 * <p>Les données chargées sont stockées dans {@link #DATASOURCE},
 * qui sert de source de données en mémoire pour l'application.</p>
 *
 * <p>Le chargement est effectué au démarrage de l'application
 * via un {@link CommandLineRunner}.</p>
 *
 * @since 1.0
 */
@Service
public class DataLoader {

    /**
     * Source de données en mémoire utilisée par l'application.
     */
    public static SafetyNetDataDTO DATASOURCE;

    /**
     * Chemin vers le fichier JSON contenant les données initiales.
     *
     * <p>La valeur est injectée depuis la configuration Spring
     * via la propriété {@code data-path}.</p>
     */
    @Value("${data-path}")
    private String datasourceFilePath;

    /**
     * Déclenche le chargement des données au démarrage de l'application.
     *
     * @return CommandLineRunner exécutant la méthode {@link #loadData()}
     */
    @Bean
    public CommandLineRunner runner() {
        return args -> loadData();
    }


    /**
     * Charge les données depuis le fichier JSON et les désérialise
     * dans {@link SafetyNetDataDTO}.
     *
     * <p>Les données sont ensuite stockées dans {@link #DATASOURCE}
     * afin d'être utilisées par les services de l'application.</p>
     *
     * @throws RuntimeException si le fichier ne peut pas être lu
     * ou si la désérialisation échoue
     */
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
