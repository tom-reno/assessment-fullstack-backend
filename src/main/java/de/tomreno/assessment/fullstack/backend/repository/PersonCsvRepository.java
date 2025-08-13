package de.tomreno.assessment.fullstack.backend.repository;

import de.tomreno.assessment.fullstack.backend.entity.Color;
import de.tomreno.assessment.fullstack.backend.entity.Person;
import de.tomreno.assessment.fullstack.backend.exception.BackendCsvException;
import de.tomreno.assessment.fullstack.backend.exception.BackendInitializationException;
import de.tomreno.assessment.fullstack.backend.parser.PersonCsvParser;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * This repository holds and manages persons from CSV sources.
 */
@Repository
@ConditionalOnProperty(value = "app.database.enabled", havingValue = "false", matchIfMissing = true)
public class PersonCsvRepository implements PersonRepository {

    private static List<Person> persons;

    private final PersonCsvParser personCsvParser;

    public PersonCsvRepository(PersonCsvParser personCsvParser) {
        this.personCsvParser = personCsvParser;
        try {
            persons = personCsvParser.readFromCsv();
        } catch (IOException e) {
            throw new BackendInitializationException("Failed to load CSV files", e);
        }
    }

    @Override
    public Page<Person> findAll(Pageable pageable) {
        return retrievePersonPageSorted(persons.stream(), pageable);
    }

    @Override
    public Page<Person> findByColor(Color color, Pageable pageable) {
        Stream<Person> personStream = persons.stream().filter(person -> person.getColor() == color);
        return retrievePersonPageSorted(personStream, pageable);
    }

    @Override
    public Optional<Person> findById(long id) {
        return persons.stream().filter(person -> person.getId() == id).findAny();
    }

    @Override
    public Page<Person> findBySearch(String search, Pageable pageable) {
        Stream<Person> personStream = persons.stream().filter(person -> containsSearch(search, person));
        return retrievePersonPageSorted(personStream, pageable);
    }

    @Override
    public Page<Person> findBySearchAndColor(String search, Color color, Pageable pageable) {
        Stream<Person> personStream = persons.stream()
                .filter(person -> containsSearch(search, person) && person.getColor() == color);
        return retrievePersonPageSorted(personStream, pageable);
    }

    @Override
    public Person save(Person entity) {
        try {
            personCsvParser.saveToCsv(entity);
            persons = personCsvParser.readFromCsv();
            return persons.getLast();
        } catch (IOException | URISyntaxException e) {
            throw new BackendCsvException("Failed to save CSV entry", e);
        }
    }

    private static boolean containsSearch(String search, Person person) {
        return person.getName().toLowerCase().contains(search.toLowerCase()) ||
                person.getLastname().toLowerCase().contains(search.toLowerCase()) ||
                person.getZipcode().toLowerCase().contains(search.toLowerCase()) ||
                person.getCity().toLowerCase().contains(search.toLowerCase());
    }

    private static PageImpl<Person> retrievePersonPageSorted(Stream<Person> personStream, Pageable pageable) {
        Sort sort = pageable.getSortOr(Sort.by(Sort.Direction.ASC, "id"));
        for (Sort.Order order : sort.toList()) {
            personStream = personStream.sorted((o1, o2) -> o1.compareWith(o2, order));
        }
        List<Person> persons = personStream.toList();
        return new PageImpl<>(persons, pageable, persons.size());
    }

}
