package com.openclassrooms.safetynetalerts.mapper;

import com.openclassrooms.safetynetalerts.dto.FirePersonDTO;
import com.openclassrooms.safetynetalerts.model.MedicalRecord;
import com.openclassrooms.safetynetalerts.model.Person;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper MapStruct responsable de la transformation des entités
 * {@link Person} et {@link MedicalRecord} vers {@link FirePersonDTO}.
 *
 * <p>Utilisé par le service associé à l'endpoint {@code GET /fire}.</p>
 *
 * <p>Le mapper est géré par Spring grâce à {@code componentModel = "spring"}.</p>
 *
 * @since 1.0
 */
@Mapper(componentModel = "spring")
public interface FireMapper {

    /**
     * Convertit une {@link Person} et son {@link MedicalRecord}
     * en {@link FirePersonDTO}.
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
    FirePersonDTO toFirePersonDTO(Person person, MedicalRecord medicalRecord, int age);

}
