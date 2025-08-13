package de.tomreno.assessment.fullstack.backend.mapper;

import de.tomreno.assessment.fullstack.backend.domain.person.PersonDto;
import de.tomreno.assessment.fullstack.backend.domain.person.PersonSaveDto;
import de.tomreno.assessment.fullstack.backend.entity.Person;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface PersonMapper {

    @Mapping(target = "color", source = "color.name")
    PersonDto toDto(Person person);

    Person toEntity(PersonSaveDto personSaveDto);

}
