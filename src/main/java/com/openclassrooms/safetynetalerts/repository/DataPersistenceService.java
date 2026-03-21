package com.openclassrooms.safetynetalerts.repository;

import com.openclassrooms.safetynetalerts.dto.SafetyNetDataDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

/**
 * Service Spring responsable de la persistance des données applicatives
 * dans le fichier JSON configuré par {@code data.path}.
 *
 * <p>Ce service sérialise l'état courant des listes en mémoire
 * {@link DataLoader#PERSONS}, {@link DataLoader#MEDICAL_RECORDS} et
 * {@link DataLoader#FIRESTATIONS} dans un {@link SafetyNetDataDTO},
 * puis réécrit le fichier JSON cible.</p>
 *
 * <p>L'écriture est réalisée via un fichier temporaire ensuite déplacé
 * vers le fichier final afin de limiter les risques de corruption
 * en cas d'erreur pendant la sauvegarde.</p>
 *
 * @since 1.0
 */
@Service
public class DataPersistenceService {

    private final ObjectMapper objectMapper;
    private final Path dataFilePath;

    /**
     * Construit le service de persistance des données.
     *
     * @param dataPath chemin vers le fichier JSON persistant configuré
     *                 dans les propriétés de l'application
     */
    public DataPersistenceService(@Value("${data.path}") String dataPath) {
        this.objectMapper = new ObjectMapper();
        this.dataFilePath = Path.of(dataPath);
    }

    /**
     * Sauvegarde l'état courant des données applicatives
     * dans le fichier JSON externe.
     *
     * <p>Les listes en mémoire sont regroupées dans un {@link SafetyNetDataDTO},
     * puis sérialisées dans un fichier temporaire avant remplacement
     * du fichier cible.</p>
     *
     * @throws RuntimeException si la sauvegarde échoue
     */
    public void saveData() {
        try {
            if (dataFilePath.getParent() != null) {
                Files.createDirectories(dataFilePath.getParent());
            }

            SafetyNetDataDTO data = new SafetyNetDataDTO();
            data.setPersons(DataLoader.PERSONS);
            data.setMedicalrecords(DataLoader.MEDICAL_RECORDS);
            data.setFirestations(DataLoader.FIRESTATIONS);

            Path tempFile = Files.createTempFile(
                    dataFilePath.getParent(),
                    "datas-",
                    ".json"
            );

            objectMapper.writeValue(tempFile.toFile(), data);

            Files.move(
                    tempFile,
                    dataFilePath,
                    StandardCopyOption.REPLACE_EXISTING,
                    StandardCopyOption.ATOMIC_MOVE
            );

        } catch (IOException e) {
            throw new RuntimeException("Failed to persist data to file: " + dataFilePath, e);
        }
    }
}