package de.tomreno.assessment.fullstack.backend.parser;

import de.tomreno.assessment.fullstack.backend.config.AppCsvPersonProps;
import de.tomreno.assessment.fullstack.backend.entity.Color;
import de.tomreno.assessment.fullstack.backend.entity.Person;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PersonCsvParserTest {

    @Mock
    private AppCsvPersonProps personCsvProps;

    @InjectMocks
    private PersonCsvParser objectUnderTest;

    @Nested
    class ParsePersonCsv {

        @Test
        void shouldParseHappyCsvFile() throws IOException {
            when(personCsvProps.getDirectory()).thenReturn("csv/happy");

            List<Person> actual = objectUnderTest.readFromCsv();

            assertThat(actual).hasSize(3);
        }

        @Test
        void shouldParseMultipleCsvFiles() throws IOException {
            when(personCsvProps.getDirectory()).thenReturn("csv/multiple");

            List<Person> actual = objectUnderTest.readFromCsv();

            assertThat(actual).hasSize(2);
        }

        @Test
        void shouldParseCsvFilesWithDifferentSeparators() throws IOException {
            when(personCsvProps.getDirectory()).thenReturn("csv/different");

            List<Person> actual = objectUnderTest.readFromCsv();

            Person person = actual.getFirst();
            assertThat(person.getName()).isEqualTo("Hans");
            assertThat(person.getLastname()).isEqualTo("Müller");
            assertThat(person.getZipcode()).isEqualTo("67742");
            assertThat(person.getCity()).isEqualTo("Lauterecken");
            assertThat(person.getColor()).isEqualTo(Color.BLUE);
        }

        @Test
        void shouldParseOnlyCsvFiles() throws IOException {
            when(personCsvProps.getDirectory()).thenReturn("csv/onlycsv");

            List<Person> actual = objectUnderTest.readFromCsv();

            assertThat(actual).hasSize(1);
        }

        @Test
        void shouldIgnoreEmptyValues() throws IOException {
            when(personCsvProps.getDirectory()).thenReturn("csv/empty");

            List<Person> actual = objectUnderTest.readFromCsv();

            Person person = actual.getFirst();
            assertThat(person.getName()).isEqualTo("Hans");
            assertThat(person.getLastname()).isEqualTo("Müller");
            assertThat(person.getZipcode()).isEqualTo("67742");
            assertThat(person.getCity()).isEqualTo("Lauterecken");
            assertThat(person.getColor()).isEqualTo(Color.BLUE);
        }

        @Test
        void shouldParseBrokenCsvFile() throws IOException {
            when(personCsvProps.getDirectory()).thenReturn("csv/broken");

            List<Person> actual = objectUnderTest.readFromCsv();

            assertThat(actual).hasSize(3);
        }

        @Test
        void shouldSkipEntriesWithUnknownColorIds() throws IOException {
            when(personCsvProps.getDirectory()).thenReturn("csv/unknown");

            List<Person> actual = objectUnderTest.readFromCsv();

            assertThat(actual).isEmpty();
        }

        @Test
        void shouldRemoveSpecialCharacters() throws IOException {
            when(personCsvProps.getDirectory()).thenReturn("csv/special");

            List<Person> actual = objectUnderTest.readFromCsv();

            Person person = actual.getFirst();
            assertThat(person.getName()).isEqualTo("Anders");
            assertThat(person.getLastname()).isEqualTo("Andersson-Meyer");
            assertThat(person.getZipcode()).isEqualTo("32132");
            assertThat(person.getCity()).isEqualTo("Schweden");
            assertThat(person.getColor()).isEqualTo(Color.GREEN);
        }

        @Test
        void shouldSplitZipcodeAndCity() throws IOException {
            when(personCsvProps.getDirectory()).thenReturn("csv/split");

            List<Person> actual = objectUnderTest.readFromCsv();

            Person person = actual.getFirst();
            assertThat(person.getZipcode()).isEqualTo("67742");
            assertThat(person.getCity()).isEqualTo("Lauterecken");
        }

    }

}
