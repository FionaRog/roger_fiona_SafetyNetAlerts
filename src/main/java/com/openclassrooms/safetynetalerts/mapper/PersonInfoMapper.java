package com.openclassrooms.safetynetalerts.mapper;

import com.openclassrooms.safetynetalerts.dto.PersonInfoDTO;
import com.openclassrooms.safetynetalerts.model.MedicalRecord;
import com.openclassrooms.safetynetalerts.model.Person;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper MapStruct responsable de la transformation des entités
 * {@link Person} et {@link MedicalRecord} vers {@link PersonInfoDTO}.
 *
 * <p>Utilisé par le service associé à l'endpoint {@code GET /personInfo}.</p>
 *
 * @since 1.0
 */
@Mapper(componentModel = "spring")
public interface PersonInfoMapper {

    /**
     * Convertit une {@link Person} et son {@link MedicalRecord}
     * en {@link PersonInfoDTO}.
     *
     * @param person personne concernée
     * @param medicalRecord dossier médical associé (peut être null)
     * @param age âge calculé de la personne
     * @return DTO correspondant
     */
    @Mapping(source = "person.firstName", target = "firstName")
    @Mapping(source = "person.lastName", target = "lastName")
    PersonInfoDTO toPersonInfoDto (Person person, MedicalRecord medicalRecord, int age);

}
