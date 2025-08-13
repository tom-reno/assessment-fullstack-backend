package de.tomreno.assessment.fullstack.backend.repository;

import de.tomreno.assessment.fullstack.backend.entity.Color;
import de.tomreno.assessment.fullstack.backend.entity.Person;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface PersonRepository {

    Page<Person> findAll(Pageable pageable);

    Page<Person> findByColor(Color color, Pageable pageable);

    Optional<Person> findById(long id);

    Page<Person> findBySearch(String search, Pageable pageable);

    Page<Person> findBySearchAndColor(String search, Color color, Pageable pageable);

    Person save(Person entity);

}
