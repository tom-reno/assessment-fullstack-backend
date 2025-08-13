package de.tomreno.assessment.fullstack.backend.service;

import de.tomreno.assessment.fullstack.backend.domain.ColorDto;
import de.tomreno.assessment.fullstack.backend.domain.person.PersonDto;
import de.tomreno.assessment.fullstack.backend.domain.person.PersonSaveDto;
import de.tomreno.assessment.fullstack.backend.entity.Color;
import de.tomreno.assessment.fullstack.backend.entity.Person;
import de.tomreno.assessment.fullstack.backend.mapper.PersonMapper;
import de.tomreno.assessment.fullstack.backend.repository.PersonRepository;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PersonServiceTest {

    @Mock
    private PersonRepository personRepository;

    @Mock
    private PersonMapper personMapper;

    @InjectMocks
    private PersonService objectUnderTest;

    @Nested
    class RetrieveAllPersons {

        @Test
        void shouldReturnPersonPageWhenPersonsExist() {
            Pageable pageable = mock(Pageable.class);
            Person person = mock(Person.class);
            PageImpl<Person> page = new PageImpl<>(List.of(person));
            when(personRepository.findAll(pageable)).thenReturn(page);
            when(personMapper.toDto(person)).thenReturn(mock(PersonDto.class));

            Page<PersonDto> actual = objectUnderTest.retrieveAllPersons(pageable);

            assertThat(actual).isNotNull();
            assertThat(actual.getContent()).hasSize(1);
            verify(personRepository).findAll(pageable);
            verify(personMapper).toDto(person);
            verifyNoMoreInteractions(personRepository, personMapper);
        }

        @Test
        void shouldReturnEmptyPageWhenPersonsNotExist() {
            Pageable pageable = mock(Pageable.class);
            when(personRepository.findAll(pageable)).thenReturn(new PageImpl<>(List.of()));

            Page<PersonDto> actual = objectUnderTest.retrieveAllPersons(pageable);

            assertThat(actual).isNotNull();
            assertThat(actual.getContent()).isEmpty();
            verify(personRepository).findAll(pageable);
            verifyNoMoreInteractions(personRepository);
            verifyNoInteractions(personMapper);
        }

    }

    @Nested
    class RetrievePersonsByColor {

        @Test
        void shouldReturnPersonPageWhenPersonsExist() {
            Pageable pageable = mock(Pageable.class);
            Person person = mock(Person.class);
            PageImpl<Person> page = new PageImpl<>(List.of(person));
            Color color = Color.BLUE;
            when(personRepository.findByColor(color, pageable)).thenReturn(page);
            when(personMapper.toDto(person)).thenReturn(mock(PersonDto.class));

            Page<PersonDto> actual = objectUnderTest.retrievePersonsByColor(ColorDto.BLUE, pageable);

            assertThat(actual).isNotNull();
            assertThat(actual.getContent()).hasSize(1);
            verify(personRepository).findByColor(color, pageable);
            verify(personMapper).toDto(person);
            verifyNoMoreInteractions(personRepository, personMapper);
        }

        @Test
        void shouldReturnEmptyPageWhenPersonsNotExist() {
            Pageable pageable = mock(Pageable.class);
            when(personRepository.findByColor(Color.BLUE, pageable)).thenReturn(new PageImpl<>(List.of()));

            Page<PersonDto> actual = objectUnderTest.retrievePersonsByColor(ColorDto.BLUE, pageable);

            assertThat(actual).isNotNull();
            assertThat(actual.getContent()).isEmpty();
            verify(personRepository).findByColor(Color.BLUE, pageable);
            verifyNoMoreInteractions(personRepository);
            verifyNoInteractions(personMapper);
        }

    }

    @Nested
    class RetrievePersonById {

        @Test
        void shouldReturnPersonOptionalWhenPersonExists() {
            long id = 1L;
            when(personRepository.findById(id)).thenReturn(Optional.of(mock(Person.class)));
            when(personMapper.toDto(any(Person.class))).thenReturn(mock(PersonDto.class));

            Optional<PersonDto> actual = objectUnderTest.retrievePersonById(id);

            assertThat(actual).isPresent();
            verify(personRepository).findById(id);
            verify(personMapper).toDto(any(Person.class));
            verifyNoMoreInteractions(personRepository, personMapper);
        }

        @Test
        void shouldReturnEmptyOptionalWhenPersonNotExists() {
            long id = 1L;
            when(personRepository.findById(id)).thenReturn(Optional.empty());

            Optional<PersonDto> actual = objectUnderTest.retrievePersonById(id);

            assertThat(actual).isNotPresent();
            verify(personRepository).findById(id);
            verifyNoMoreInteractions(personRepository);
            verifyNoInteractions(personMapper);
        }

    }

    @Nested
    class RetrievePersonsBySearch {

        @Test
        void shouldReturnPersonPageWhenColorIsNotNullAndPersonsExist() {
            Pageable pageable = mock(Pageable.class);
            Person person = mock(Person.class);
            String search = "search";
            Color color = Color.BLUE;
            when(personRepository.findBySearchAndColor(search, color, pageable)).thenReturn(new PageImpl<>(List.of(person)));
            when(personMapper.toDto(person)).thenReturn(mock(PersonDto.class));

            Page<PersonDto> actual = objectUnderTest.retrievePersonsBySearch(search, ColorDto.BLUE, pageable);

            assertThat(actual).isNotNull();
            assertThat(actual.getContent()).hasSize(1);
            verify(personRepository).findBySearchAndColor(search, color, pageable);
            verify(personMapper).toDto(person);
            verifyNoMoreInteractions(personRepository, personMapper);
        }

        @Test
        void shouldReturnEmptyPageWhenColorIsNotNullAndPersonsNotExist() {
            Pageable pageable = mock(Pageable.class);
            String search = "search";
            Color color = Color.BLUE;
            when(personRepository.findBySearchAndColor(search, color, pageable)).thenReturn(new PageImpl<>(List.of()));

            Page<PersonDto> actual = objectUnderTest.retrievePersonsBySearch(search, ColorDto.BLUE, pageable);

            assertThat(actual).isNotNull();
            assertThat(actual.getContent()).isEmpty();
            verify(personRepository).findBySearchAndColor(search, color, pageable);
            verifyNoMoreInteractions(personRepository);
            verifyNoInteractions(personMapper);
        }

        @Test
        void shouldReturnPersonPageWhenColorIsNullAndPersonsExist() {
            Pageable pageable = mock(Pageable.class);
            Person person = mock(Person.class);
            String search = "search";
            when(personRepository.findBySearch(search, pageable)).thenReturn(new PageImpl<>(List.of(person)));
            when(personMapper.toDto(person)).thenReturn(mock(PersonDto.class));

            Page<PersonDto> actual = objectUnderTest.retrievePersonsBySearch(search, null, pageable);

            assertThat(actual).isNotNull();
            assertThat(actual.getContent()).hasSize(1);
            verify(personRepository).findBySearch(search, pageable);
            verify(personMapper).toDto(person);
            verifyNoMoreInteractions(personRepository, personMapper);
        }

        @Test
        void shouldReturnEmptyPageWhenColorIsNullAndPersonsNotExist() {
            Pageable pageable = mock(Pageable.class);
            String search = "search";
            when(personRepository.findBySearch(search, pageable)).thenReturn(new PageImpl<>(List.of()));

            Page<PersonDto> actual = objectUnderTest.retrievePersonsBySearch(search, null, pageable);

            assertThat(actual).isNotNull();
            assertThat(actual.getContent()).isEmpty();
            verify(personRepository).findBySearch(search, pageable);
            verifyNoMoreInteractions(personRepository);
            verifyNoInteractions(personMapper);
        }

    }

    @Nested
    class SavePerson {

        @Test
        void shouldReturnPerson() {
            PersonSaveDto personSaveDto = mock(PersonSaveDto.class);
            Person person = mock(Person.class);
            when(personMapper.toEntity(personSaveDto)).thenReturn(person);
            when(personRepository.save(person)).thenReturn(person);
            when(personMapper.toDto(person)).thenReturn(mock(PersonDto.class));

            PersonDto actual = objectUnderTest.savePerson(personSaveDto);

            assertThat(actual).isNotNull();
            verify(personMapper).toEntity(personSaveDto);
            verify(personRepository).save(person);
            verify(personMapper).toDto(person);
            verifyNoMoreInteractions(personRepository, personMapper);
        }

    }

}
