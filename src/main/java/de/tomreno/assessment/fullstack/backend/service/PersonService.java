package de.tomreno.assessment.fullstack.backend.service;

import de.tomreno.assessment.fullstack.backend.domain.ColorDto;
import de.tomreno.assessment.fullstack.backend.domain.person.PersonDto;
import de.tomreno.assessment.fullstack.backend.domain.person.PersonSaveDto;
import de.tomreno.assessment.fullstack.backend.entity.Color;
import de.tomreno.assessment.fullstack.backend.entity.Person;
import de.tomreno.assessment.fullstack.backend.mapper.PersonMapper;
import de.tomreno.assessment.fullstack.backend.repository.PersonRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PersonService {

    private final PersonRepository personRepository;
    private final PersonMapper personMapper;

    public PersonService(PersonRepository personRepository, PersonMapper personMapper) {
        this.personRepository = personRepository;
        this.personMapper = personMapper;
    }

    public Page<PersonDto> retrieveAllPersons(Pageable pageable) {
        return personRepository.findAll(pageable).map(personMapper::toDto);
    }

    public Optional<PersonDto> retrievePersonById(long id) {
        return personRepository.findById(id).map(personMapper::toDto);
    }

    public Page<PersonDto> retrievePersonsByColor(ColorDto color, Pageable pageable) {
        return personRepository.findByColor(Color.valueOf(color.name()), pageable).map(personMapper::toDto);
    }

    public Page<PersonDto> retrievePersonsBySearch(String search, ColorDto color, Pageable pageable) {
        return Optional.ofNullable(color)
                .map(c -> personRepository.findBySearchAndColor(search, Color.valueOf(c.name()), pageable))
                .orElseGet(() -> personRepository.findBySearch(search, pageable))
                .map(personMapper::toDto);
    }

    public PersonDto savePerson(PersonSaveDto personSaveDto) {
        Person entity = personMapper.toEntity(personSaveDto);
        Person savedEntity = personRepository.save(entity);
        return personMapper.toDto(savedEntity);
    }

}
