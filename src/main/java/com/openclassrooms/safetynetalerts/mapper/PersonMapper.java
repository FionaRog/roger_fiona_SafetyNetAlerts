package com.openclassrooms.safetynetalerts.mapper;

import com.openclassrooms.safetynetalerts.dto.AdultDTO;
import com.openclassrooms.safetynetalerts.dto.ChildAlertDTO;
import com.openclassrooms.safetynetalerts.model.Person;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PersonMapper {

    AdultDTO toAdultDto (Person person);

    @Mapping(target = "age", ignore = true)
    @Mapping(target = "householdMembers", ignore = true)
    ChildAlertDTO toChildAlertDto(Person person);

}
