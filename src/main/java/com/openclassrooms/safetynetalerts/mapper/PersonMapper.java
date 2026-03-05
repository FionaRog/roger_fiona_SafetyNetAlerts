package com.openclassrooms.safetynetalerts.mapper;

import com.openclassrooms.safetynetalerts.dto.AdultDTO;
import com.openclassrooms.safetynetalerts.dto.ChildAlertDTO;
import com.openclassrooms.safetynetalerts.model.Person;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * Mapper MapStruct responsable de la transformation
 * des entités {@link Person} vers les DTO utilisés
 * par l'endpoint {@code /childAlert}.
 *
 * @since 1.0
 */
@Mapper(componentModel = "spring")
public interface PersonMapper {
    /**
     * Convertit une {@link Person} en {@link AdultDTO}.
     *
     * @param person personne à convertir
     * @return DTO adulte correspondant
     */
    AdultDTO toAdultDto (Person person);

    /**
     * Convertit une {@link Person} en {@link ChildAlertDTO}.
     *
     * <p>L'âge et les membres du foyer sont ignorés ici
     * et sont renseignés par la couche service.</p>
     *
     * @param person personne à convertir
     * @return DTO enfant correspondant
     */
    @Mapping(target = "age", ignore = true)
    @Mapping(target = "householdMembers", ignore = true)
    ChildAlertDTO toChildAlertDto(Person person);

}
