package com.openclassrooms.safetynetalerts;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Point d'entrée de l'application Spring Boot SafetyNetAlerts.
 *
 * <p>Cette classe initialise et démarre le contexte Spring grâce à
 * l'annotation {@link SpringBootApplication}.</p>
 *
 * <p>Le démarrage de l'application est effectué via la méthode
 * {@link SpringApplication#run(Class, String...)} qui initialise
 * l'ensemble des composants Spring (controllers, services, mappers, etc.).</p>
 *
 * @since 1.0
 */
@SpringBootApplication
public class SafetyNetAlertsApplication {

	/**
	 * Méthode principale permettant de lancer l'application.
	 *
	 * @param args arguments de ligne de commande
	 */
	public static void main(String[] args) {
		SpringApplication.run(SafetyNetAlertsApplication.class, args);
	}

}
