package com.openclassrooms.safetynetalerts.service.endpoint;

import com.openclassrooms.safetynetalerts.dto.PersonInfoDTO;
import com.openclassrooms.safetynetalerts.model.MedicalRecord;
import com.openclassrooms.safetynetalerts.model.Person;

import java.util.List;

/**
 * Contrat du service associé à l'endpoint {@code /personInfo}.
 *
 * <p>Retourne une liste de DTO construits à partir des informations de {@link Person}
 * et du {@link MedicalRecord} correspondant (si disponible), incluant l'âge calculé.</p>
 *
 * <p>Si le paramètre {@code lastName} est null/blanc ou si aucun résultat n'est trouvé,
 * une liste vide est retournée.</p>
 *
 * @since 1.0
 */
public interface IPersonInfoService {

    /**
     * Retourne les informations des personnes correspondant au nom de famille fourni.
     *
     * @param lastName nom de famille recherché (peut être null/blanc)
     * @return liste de {@link PersonInfoDTO} (peut être vide)
     * @since 1.0
     */
    List<PersonInfoDTO> getPersonsByLastName (String lastName);
}
