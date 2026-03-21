package com.openclassrooms.safetynetalerts.repository;

import com.openclassrooms.safetynetalerts.dto.SafetyNetDataDTO;
import com.openclassrooms.safetynetalerts.model.Firestation;
import com.openclassrooms.safetynetalerts.model.MedicalRecord;
import com.openclassrooms.safetynetalerts.model.Person;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Composant Spring responsable du chargement et de l'initialisation des données
 * de l'application SafetyNetAlerts.
 *
 * <p>Les données sont lues depuis un fichier JSON externe défini par {@code data.path}.
 * Si ce fichier n'existe pas, il est initialisé à partir d'un fichier seed
 * présent dans le classpath ({@code data.classpath-seed}).</p>
 *
 * <p>Les données sont ensuite désérialisées en {@link SafetyNetDataDTO}
 * à l'aide de Jackson.</p>
 *
 * <p>Les données sont stockées et réparties dans trois collections statiques
 * en mémoire :</p>
 * <ul>
 *      <li>{@link #PERSONS}</li>
 *      <li>{@link #MEDICAL_RECORDS}</li>
 *      <li>{@link #FIRESTATIONS}</li>
 * </ul>
 *
 *  <p>Cette organisation permet de séparer les données initiales des données persistantes,
 * de permettre la modification et la persistance des données à l'exécution,
 * d'éviter les limitations du classpath.</p>
 *
 * <p>Le chargement est effectué au démarrage de l'application
 * via un {@link CommandLineRunner}.</p>
 *
 * @since 1.0
 */
@Service
public class DataLoader {

    private static final Logger logger = LoggerFactory.getLogger(DataLoader.class);


    /**
     * Données en mémoire utilisées par l'application.
     */

    public static List<Person> PERSONS = new ArrayList<>();
    public static List<MedicalRecord> MEDICAL_RECORDS = new ArrayList<>();
    public static List<Firestation> FIRESTATIONS = new ArrayList<>();


    /**
     * Nom du fichier JSON initial présent dans le classpath.
     *
     * <p>Utilisé uniquement pour initialiser le fichier de données externe
     * lors du premier lancement de l'application.</p>
     */
    @Value("${data.classpath-seed}")
    private String classpathSeed;

    /**
     * Chemin vers le fichier de données persistant utilisé par l'application.
     *
     * <p>Ce fichier est lu au démarrage pour charger les données en mémoire.
     * S'il n'existe pas, il est automatiquement créé à partir du fichier seed
     * présent dans le classpath.</p>
     */
    @Value("${data.path}")
    private String dataFilePath;

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
     * Charge les données applicatives depuis le fichier JSON externe.
     *
     * <p>Étapes :</p>
     * <ul>
     *   <li>vérifie l'existence du fichier externe défini par {@code data.path},</li>
     *   <li>si absent, copie le fichier seed depuis le classpath,</li>
     *   <li>lit le fichier JSON externe,</li>
     *   <li>désérialise les données en objets métier,</li>
     *   <li>alimente les listes statiques {@code PERSONS}, {@code MEDICAL_RECORDS} et {@code FIRESTATIONS}.</li>
     * </ul>
     *
     * <p>En cas d'échec, une exception est levée et l'application ne peut pas démarrer correctement.</p>
     *
     * @throws RuntimeException si le chargement des données échoue
     */
    public void loadData() {

        logger.info("Loading data from persistent file path: {}", dataFilePath);

        ObjectMapper objectMapper = new ObjectMapper();

        try {
            Path targetPath = Path.of(dataFilePath);
            if (targetPath.getParent() !=null) {
                Files.createDirectories(targetPath.getParent());
            }

            if(Files.notExists(targetPath)) {
                logger.info("Persistent data file not found, initializing from seed file : {}",
                        classpathSeed);

                ClassPathResource resource = new ClassPathResource(classpathSeed);
                if(!resource.exists()) {
                    throw new RuntimeException("Seed file not found in classpath: " + classpathSeed);
                }

                try (InputStream is = resource.getInputStream()) {
                    Files.copy(is, targetPath);
                }

                logger.info("Persistent data file created at: {}", targetPath.toAbsolutePath());
            }


            try (InputStream is = Files.newInputStream(targetPath)) {
                SafetyNetDataDTO loadedData = objectMapper.readValue(is, new TypeReference<SafetyNetDataDTO>() {});
                MEDICAL_RECORDS = loadedData.getMedicalrecords();
                PERSONS = loadedData.getPersons();
                FIRESTATIONS = loadedData.getFirestations();
            }

            int personsCount = PERSONS == null ? 0 : PERSONS.size();
            int firestationsCount = FIRESTATIONS == null ? 0 : FIRESTATIONS.size();
            int medicalCount = MEDICAL_RECORDS == null ? 0 : MEDICAL_RECORDS.size();

            logger.info("Data loaded from persistent file: {} persons={}, firestations={}, medicalrecords={}",
                    targetPath.toAbsolutePath(), personsCount, firestationsCount, medicalCount);

        } catch (Exception e) {
            logger.error("Failed to load data from persistent file: {}", dataFilePath, e);
            throw new RuntimeException("Failed to load data from persistent file: " + dataFilePath, e);
        }
    }
}
