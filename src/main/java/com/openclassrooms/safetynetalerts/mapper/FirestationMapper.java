package com.openclassrooms.safetynetalerts.mapper;

import com.openclassrooms.safetynetalerts.dto.FirestationPersonDTO;
import com.openclassrooms.safetynetalerts.model.Person;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * Mapper MapStruct responsable de la transformation
 * d'une {@link Person} vers {@link FirestationPersonDTO}.
 *
 * <p>Utilisé par le service associé à l'endpoint {@code GET /firestation}.</p>
 *
 * @since 1.0
 */
@Mapper(componentModel = "spring")
public interface FirestationMapper {
    /**
     * Convertit une {@link Person} en {@link FirestationPersonDTO}.
     *
     * @param person personne à convertir
     * @return DTO correspondant
     */
    FirestationPersonDTO toFirestationPersonDTO(Person person);

    /**
     * Convertit une liste de {@link Person} en liste de {@link FirestationPersonDTO}.
     *
     * @param persons liste des personnes à convertir
     * @return liste des DTO correspondants
     */
    List<FirestationPersonDTO> toFirestationPersonDTOList(List<Person> persons);

}
