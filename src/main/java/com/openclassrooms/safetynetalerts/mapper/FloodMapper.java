package com.openclassrooms.safetynetalerts.mapper;

import com.openclassrooms.safetynetalerts.dto.FloodPersonDTO;
import com.openclassrooms.safetynetalerts.model.MedicalRecord;
import com.openclassrooms.safetynetalerts.model.Person;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper MapStruct responsable de la transformation des entités
 * {@link Person} et {@link MedicalRecord} vers {@link FloodPersonDTO}.
 *
 * <p>Utilisé par le service associé à l'endpoint {@code GET /flood/stations}.</p>
 *
 * @since 1.0
 */
@Mapper(componentModel = "spring")
public interface FloodMapper {

    /**
     * Convertit une {@link Person} et son {@link MedicalRecord}
     * en {@link FloodPersonDTO}.
     *
     * @param person        personne concernée
     * @param medicalRecord dossier médical associé (peut être null)
     * @param age           âge calculé de la personne
     * @return DTO correspondant
     */
    @Mapping(source = "person.firstName", target = "firstName")
    @Mapping(source = "person.lastName", target = "lastName")
    @Mapping(source = "person.phone", target = "phone")
    @Mapping(source = "medicalRecord.medications", target = "medications")
    @Mapping(source = "medicalRecord.allergies", target = "allergies")
    @Mapping(source = "age", target = "age")
    FloodPersonDTO toFloodPersonDTO(Person person, MedicalRecord medicalRecord, int age);

}
