package com.openclassrooms.safetynetalerts.mapper;

import com.openclassrooms.safetynetalerts.dto.FirestationPersonDTO;
import com.openclassrooms.safetynetalerts.model.Person;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface FirestationMapper {
    FirestationPersonDTO toFirestationPersonDTO(Person person);

    List<FirestationPersonDTO> toFirestationPersonDTOList(List<Person> persons);

}
