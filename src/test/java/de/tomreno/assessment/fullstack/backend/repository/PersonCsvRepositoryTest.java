package de.tomreno.assessment.fullstack.backend.repository;

import de.tomreno.assessment.fullstack.backend.entity.Color;
import de.tomreno.assessment.fullstack.backend.entity.Person;
import de.tomreno.assessment.fullstack.backend.exception.BackendCsvException;
import de.tomreno.assessment.fullstack.backend.exception.BackendInitializationException;
import de.tomreno.assessment.fullstack.backend.parser.PersonCsvParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PersonCsvRepositoryTest {

    @Mock
    PersonCsvParser personCsvParser;

    PersonCsvRepository objectUnderTest;

    @BeforeEach
    void setUp() throws Exception {
        try (@SuppressWarnings("unused") AutoCloseable autoCloseable = MockitoAnnotations.openMocks(this)) {
            List<Person> persons = createPersonList();
            when(personCsvParser.readFromCsv()).thenReturn(persons);
            objectUnderTest = new PersonCsvRepository(personCsvParser);
        }
    }

    @Nested
    class Constructor {

        @Test
        void shouldInitializePersonsList() throws IOException {
            verify(personCsvParser).readFromCsv();
            verifyNoMoreInteractions(personCsvParser);
        }

        @Test
        void shouldThrowBackendInitializationExceptionWhenPersonCsvParserReadFromCsvThrowsIOException() throws IOException {
            doThrow(IOException.class).when(personCsvParser).readFromCsv();

            Throwable thrown = catchException(() -> new PersonCsvRepository(personCsvParser));

            assertThat(thrown).isInstanceOf(BackendInitializationException.class);
            assertThat(thrown.getMessage()).isEqualTo("Failed to load CSV files");
            verify(personCsvParser, times(2)).readFromCsv(); // Twice, because it reads on instantiation first
            verifyNoMoreInteractions(personCsvParser);
        }

    }

    @Nested
    class FindAll {

        @Test
        void shouldReturnPersonPage() {
            Pageable pageable = mock(Pageable.class);
            when(pageable.getSortOr(any(Sort.class))).thenReturn(Sort.by(Sort.Direction.ASC, "id"));

            Page<Person> actual = objectUnderTest.findAll(pageable);

            assertThat(actual.getContent()).hasSize(5);
        }

        @Test
        void shouldSortByIdAsc() {
            Pageable pageable = mock(Pageable.class);
            when(pageable.getSortOr(any(Sort.class))).thenReturn(Sort.by(Sort.Direction.ASC, "id"));

            Page<Person> actual = objectUnderTest.findAll(pageable);

            List<Person> persons = actual.getContent();
            assertThat(persons.get(0).getId()).isEqualTo(1);
            assertThat(persons.get(1).getId()).isEqualTo(2);
            assertThat(persons.get(2).getId()).isEqualTo(3);
            assertThat(persons.get(3).getId()).isEqualTo(4);
            assertThat(persons.get(4).getId()).isEqualTo(5);
        }

        @Test
        void shouldSortByIdDesc() {
            Pageable pageable = mock(Pageable.class);
            when(pageable.getSortOr(any(Sort.class))).thenReturn(Sort.by(Sort.Direction.DESC, "id"));

            Page<Person> actual = objectUnderTest.findAll(pageable);

            List<Person> persons = actual.getContent();
            assertThat(persons.get(0).getId()).isEqualTo(5);
            assertThat(persons.get(1).getId()).isEqualTo(4);
            assertThat(persons.get(2).getId()).isEqualTo(3);
            assertThat(persons.get(3).getId()).isEqualTo(2);
            assertThat(persons.get(4).getId()).isEqualTo(1);
        }

        @Test
        void shouldSortByNameAsc() {
            Pageable pageable = mock(Pageable.class);
            when(pageable.getSortOr(any(Sort.class))).thenReturn(Sort.by(Sort.Direction.ASC, "name"));

            Page<Person> actual = objectUnderTest.findAll(pageable);

            List<Person> persons = actual.getContent();
            assertThat(persons.get(0).getName()).isEqualTo("Bernd");
            assertThat(persons.get(1).getName()).isEqualTo("Claudia");
            assertThat(persons.get(2).getName()).isEqualTo("Hans");
            assertThat(persons.get(3).getName()).isEqualTo("Sansa");
            assertThat(persons.get(4).getName()).isEqualTo("Thomas");
        }

        @Test
        void shouldSortByNameDesc() {
            Pageable pageable = mock(Pageable.class);
            when(pageable.getSortOr(any(Sort.class))).thenReturn(Sort.by(Sort.Direction.DESC, "name"));

            Page<Person> actual = objectUnderTest.findAll(pageable);

            List<Person> persons = actual.getContent();
            assertThat(persons.get(0).getName()).isEqualTo("Thomas");
            assertThat(persons.get(1).getName()).isEqualTo("Sansa");
            assertThat(persons.get(2).getName()).isEqualTo("Hans");
            assertThat(persons.get(3).getName()).isEqualTo("Claudia");
            assertThat(persons.get(4).getName()).isEqualTo("Bernd");
        }

        @Test
        void shouldSortByLastnameAsc() {
            Pageable pageable = mock(Pageable.class);
            when(pageable.getSortOr(any(Sort.class))).thenReturn(Sort.by(Sort.Direction.ASC, "lastname"));

            Page<Person> actual = objectUnderTest.findAll(pageable);

            List<Person> persons = actual.getContent();
            assertThat(persons.get(0).getLastname()).isEqualTo("Hansen");
            assertThat(persons.get(1).getLastname()).isEqualTo("Hinz");
            assertThat(persons.get(2).getLastname()).isEqualTo("Reno");
            assertThat(persons.get(3).getLastname()).isEqualTo("Stark");
            assertThat(persons.get(4).getLastname()).isEqualTo("Wurst");
        }

        @Test
        void shouldSortByLastnameDesc() {
            Pageable pageable = mock(Pageable.class);
            when(pageable.getSortOr(any(Sort.class))).thenReturn(Sort.by(Sort.Direction.DESC, "lastname"));

            Page<Person> actual = objectUnderTest.findAll(pageable);

            List<Person> persons = actual.getContent();
            assertThat(persons.get(0).getLastname()).isEqualTo("Wurst");
            assertThat(persons.get(1).getLastname()).isEqualTo("Stark");
            assertThat(persons.get(2).getLastname()).isEqualTo("Reno");
            assertThat(persons.get(3).getLastname()).isEqualTo("Hinz");
            assertThat(persons.get(4).getLastname()).isEqualTo("Hansen");
        }

        @Test
        void shouldSortByZipcodeAsc() {
            Pageable pageable = mock(Pageable.class);
            when(pageable.getSortOr(any(Sort.class))).thenReturn(Sort.by(Sort.Direction.ASC, "zipcode"));

            Page<Person> actual = objectUnderTest.findAll(pageable);

            List<Person> persons = actual.getContent();
            assertThat(persons.get(0).getZipcode()).isEqualTo("12345");
            assertThat(persons.get(1).getZipcode()).isEqualTo("18435");
            assertThat(persons.get(2).getZipcode()).isEqualTo("55443");
            assertThat(persons.get(3).getZipcode()).isEqualTo("87342");
            assertThat(persons.get(4).getZipcode()).isEqualTo("98765");
        }

        @Test
        void shouldSortByZipcodeDesc() {
            Pageable pageable = mock(Pageable.class);
            when(pageable.getSortOr(any(Sort.class))).thenReturn(Sort.by(Sort.Direction.DESC, "zipcode"));

            Page<Person> actual = objectUnderTest.findAll(pageable);

            List<Person> persons = actual.getContent();
            assertThat(persons.get(0).getZipcode()).isEqualTo("98765");
            assertThat(persons.get(1).getZipcode()).isEqualTo("87342");
            assertThat(persons.get(2).getZipcode()).isEqualTo("55443");
            assertThat(persons.get(3).getZipcode()).isEqualTo("18435");
            assertThat(persons.get(4).getZipcode()).isEqualTo("12345");
        }

        @Test
        void shouldSortByCityAsc() {
            Pageable pageable = mock(Pageable.class);
            when(pageable.getSortOr(any(Sort.class))).thenReturn(Sort.by(Sort.Direction.ASC, "city"));

            Page<Person> actual = objectUnderTest.findAll(pageable);

            List<Person> persons = actual.getContent();
            assertThat(persons.get(0).getCity()).isEqualTo("Assessment");
            assertThat(persons.get(1).getCity()).isEqualTo("Hansestadt");
            assertThat(persons.get(2).getCity()).isEqualTo("Stralsund");
            assertThat(persons.get(3).getCity()).isEqualTo("Tnemssessa");
            assertThat(persons.get(4).getCity()).isEqualTo("Winterfell");
        }

        @Test
        void shouldSortByCityDesc() {
            Pageable pageable = mock(Pageable.class);
            when(pageable.getSortOr(any(Sort.class))).thenReturn(Sort.by(Sort.Direction.DESC, "city"));

            Page<Person> actual = objectUnderTest.findAll(pageable);

            List<Person> persons = actual.getContent();
            assertThat(persons.get(0).getCity()).isEqualTo("Winterfell");
            assertThat(persons.get(1).getCity()).isEqualTo("Tnemssessa");
            assertThat(persons.get(2).getCity()).isEqualTo("Stralsund");
            assertThat(persons.get(3).getCity()).isEqualTo("Hansestadt");
            assertThat(persons.get(4).getCity()).isEqualTo("Assessment");
        }

        @Test
        void shouldSortByColorAsc() {
            Pageable pageable = mock(Pageable.class);
            when(pageable.getSortOr(any(Sort.class))).thenReturn(Sort.by(Sort.Direction.ASC, "color"));

            Page<Person> actual = objectUnderTest.findAll(pageable);

            List<Person> persons = actual.getContent();
            assertThat(persons.get(0).getColor().getName()).isEqualTo("blau");
            assertThat(persons.get(1).getColor().getName()).isEqualTo("gelb");
            assertThat(persons.get(2).getColor().getName()).isEqualTo("grün");
            assertThat(persons.get(3).getColor().getName()).isEqualTo("rot");
            assertThat(persons.get(4).getColor().getName()).isEqualTo("weiß");
        }

        @Test
        void shouldSortByColorDesc() {
            Pageable pageable = mock(Pageable.class);
            when(pageable.getSortOr(any(Sort.class))).thenReturn(Sort.by(Sort.Direction.DESC, "color"));

            Page<Person> actual = objectUnderTest.findAll(pageable);

            List<Person> persons = actual.getContent();
            assertThat(persons.get(0).getColor().getName()).isEqualTo("weiß");
            assertThat(persons.get(1).getColor().getName()).isEqualTo("rot");
            assertThat(persons.get(2).getColor().getName()).isEqualTo("grün");
            assertThat(persons.get(3).getColor().getName()).isEqualTo("gelb");
            assertThat(persons.get(4).getColor().getName()).isEqualTo("blau");
        }

    }

    @Nested
    class FindByColor {

        @Test
        void shouldFindPersonsByColor() {
            Color color = Color.BLUE;
            Pageable pageable = mock(Pageable.class);
            when(pageable.getSortOr(any(Sort.class))).thenReturn(Sort.by(Sort.Direction.ASC, "id"));

            Page<Person> actual = objectUnderTest.findByColor(color, pageable);

            assertThat(actual.getContent().getFirst().getColor()).isEqualTo(color);
        }

    }

    @Nested
    class FindById {

        @Test
        void shouldFindPersonOptionalWhenPersonByIdExist() {
            long id = 1L;

            Optional<Person> actual = objectUnderTest.findById(id);

            assertThat(actual).isPresent();
            assertThat(actual.get().getId()).isEqualTo(id);
        }

        @Test
        void shouldFindEmptyOptionalWhenPersonByIdNotExist() {
            long id = 6L;

            Optional<Person> actual = objectUnderTest.findById(id);

            assertThat(actual).isEmpty();
        }

    }

    @Nested
    class FindBySearch {

        @Test
        void shouldFindPersonsWhenPersonsExist() {
            Pageable pageable = mock(Pageable.class);
            when(pageable.getSortOr(any(Sort.class))).thenReturn(Sort.by(Sort.Direction.ASC, "id"));

            Page<Person> actual = objectUnderTest.findBySearch("hans", pageable);

            List<Person> persons = actual.getContent();
            assertThat(persons).hasSize(3);
            assertThat(persons.get(0).getName()).isEqualTo("Hans");
            assertThat(persons.get(1).getLastname()).isEqualTo("Hansen");
            assertThat(persons.get(2).getCity()).isEqualTo("Hansestadt");
        }

    }

    @Nested
    class FindBySearchAndColor {

        @Test
        void shouldFindPersonsWhenPersonsExist() {
            Pageable pageable = mock(Pageable.class);
            when(pageable.getSortOr(any(Sort.class))).thenReturn(Sort.by(Sort.Direction.ASC, "id"));

            Page<Person> actual = objectUnderTest.findBySearchAndColor("hans", Color.BLUE, pageable);

            List<Person> persons = actual.getContent();
            assertThat(persons).hasSize(1);
            Person person = persons.getFirst();
            assertThat(person.getName()).isEqualTo("Hans");
            assertThat(person.getColor()).isEqualTo(Color.BLUE);
        }

    }

    @Nested
    class Save {

        @Test
        void shouldReturnSavedPerson() throws IOException, URISyntaxException {
            Person person = createPerson(1L, "Kunigunde", "Grundwitz", "10439", "Berlin", Color.GREEN);
            List<Person> personList = createPersonList();
            personList.add(person);
            when(personCsvParser.readFromCsv()).thenReturn(personList);

            Person actual = objectUnderTest.save(person);

            assertThat(actual).isNotNull();
            assertThat(actual).isEqualTo(person);
            verify(personCsvParser).saveToCsv(person);
            verify(personCsvParser, times(2)).readFromCsv(); // Twice, because it reads on instantiation first
            verifyNoMoreInteractions(personCsvParser);
        }

        @Test
        void shouldThrowBackendCsvExceptionWhenPersonCsvParserSaveToCsvThrowsIOException() throws IOException, URISyntaxException {
            Person person = mock(Person.class);
            doThrow(IOException.class).when(personCsvParser).saveToCsv(person);

            Throwable thrown = catchException(() -> objectUnderTest.save(person));

            assertThat(thrown).isInstanceOf(BackendCsvException.class);
            assertThat(thrown.getMessage()).isEqualTo("Failed to save CSV entry");
            verify(personCsvParser).saveToCsv(person);
            verifyNoMoreInteractions(personCsvParser);
            verifyNoMoreInteractions(personCsvParser);
        }

        @Test
        void shouldThrowBackendCsvExceptionWhenPersonCsvParserSaveToCsvThrowsURISyntaxException() throws IOException, URISyntaxException {
            Person person = mock(Person.class);
            doThrow(URISyntaxException.class).when(personCsvParser).saveToCsv(person);

            Throwable thrown = catchException(() -> objectUnderTest.save(person));

            assertThat(thrown).isInstanceOf(BackendCsvException.class);
            assertThat(thrown.getMessage()).isEqualTo("Failed to save CSV entry");
            verify(personCsvParser).saveToCsv(person);
            verifyNoMoreInteractions(personCsvParser);
            verifyNoMoreInteractions(personCsvParser);
        }

        @Test
        void shouldThrowBackendCsvExceptionWhenPersonCsvParserReadFromCsvThrowsIOException() throws IOException, URISyntaxException {
            Person person = mock(Person.class);
            doThrow(IOException.class).when(personCsvParser).saveToCsv(person);

            Throwable thrown = catchException(() -> objectUnderTest.save(person));

            assertThat(thrown).isInstanceOf(BackendCsvException.class);
            assertThat(thrown.getMessage()).isEqualTo("Failed to save CSV entry");
            verify(personCsvParser).saveToCsv(person);
            verifyNoMoreInteractions(personCsvParser);
            verifyNoMoreInteractions(personCsvParser);
        }

    }

    private static List<Person> createPersonList() {
        List<Person> persons = new ArrayList<>();
        persons.add(createPerson(1, "Hans", "Wurst", "12345", "Assessment", Color.BLUE));
        persons.add(createPerson(2, "Bernd", "Hansen", "98765", "Tnemssessa", Color.GREEN));
        persons.add(createPerson(3, "Sansa", "Stark", "55443", "Winterfell", Color.RED));
        persons.add(createPerson(4, "Thomas", "Reno", "18435", "Stralsund", Color.YELLOW));
        persons.add(createPerson(5, "Claudia", "Hinz", "87342", "Hansestadt", Color.WHITE));
        return persons;
    }

    private static Person createPerson(long id, String name, String lastname, String zipcode, String city, Color color) {
        Person person = new Person(id);
        person.setName(name);
        person.setLastname(lastname);
        person.setZipcode(zipcode);
        person.setCity(city);
        person.setColor(color);
        return person;
    }

}
