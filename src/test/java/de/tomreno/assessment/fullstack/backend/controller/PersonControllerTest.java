package de.tomreno.assessment.fullstack.backend.controller;

import de.tomreno.assessment.fullstack.backend.domain.ColorDto;
import de.tomreno.assessment.fullstack.backend.domain.person.PersonDto;
import de.tomreno.assessment.fullstack.backend.domain.person.PersonSaveDto;
import de.tomreno.assessment.fullstack.backend.service.PersonService;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PersonControllerTest {

    @Mock
    private PersonService personService;

    @InjectMocks
    private PersonController objectUnderTest;

    @Nested
    class GetAll {

        @Test
        void shouldReturnPersonDtoPageWhenPersonsExist() {
            Pageable pageable = mock(Pageable.class);
            PersonDto personDto = mock(PersonDto.class);
            when(personService.retrieveAllPersons(pageable)).thenReturn(new PageImpl<>(List.of(personDto)));

            Page<PersonDto> actual = objectUnderTest.getAll(pageable);

            assertThat(actual).isNotNull();
            assertThat(actual.getContent()).hasSize(1);
            assertThat(actual.getContent().getFirst()).isEqualTo(personDto);
            verify(personService).retrieveAllPersons(pageable);
            verifyNoMoreInteractions(personService);
        }

        @Test
        void shouldReturnEmptyPageWhenPersonsNotExist() {
            Pageable pageable = mock(Pageable.class);
            when(personService.retrieveAllPersons(pageable)).thenReturn(new PageImpl<>(List.of()));

            Page<PersonDto> actual = objectUnderTest.getAll(pageable);

            assertThat(actual).isNotNull();
            assertThat(actual.getContent()).isEmpty();
            verify(personService).retrieveAllPersons(pageable);
            verifyNoMoreInteractions(personService);
        }

    }

    @Nested
    class GetAllByColor {

        @Test
        void shouldReturnPersonDtoPageWhenPersonsExist() {
            Pageable pageable = mock(Pageable.class);
            PersonDto personDto = mock(PersonDto.class);
            ColorDto color = ColorDto.BLUE;
            when(personService.retrievePersonsByColor(color, pageable)).thenReturn(new PageImpl<>(List.of(personDto)));

            Page<PersonDto> actual = objectUnderTest.getAllByColor(color, pageable);

            assertThat(actual).isNotNull();
            assertThat(actual.getContent()).hasSize(1);
            assertThat(actual.getContent().getFirst()).isEqualTo(personDto);
            verify(personService).retrievePersonsByColor(color, pageable);
            verifyNoMoreInteractions(personService);
        }

        @Test
        void shouldReturnEmptyPageWhenPersonsNotExist() {
            Pageable pageable = mock(Pageable.class);
            ColorDto color = ColorDto.BLUE;
            when(personService.retrievePersonsByColor(color, pageable)).thenReturn(new PageImpl<>(List.of()));

            Page<PersonDto> actual = objectUnderTest.getAllByColor(color, pageable);

            assertThat(actual).isNotNull();
            assertThat(actual.getContent()).isEmpty();
            verify(personService).retrievePersonsByColor(color, pageable);
            verifyNoMoreInteractions(personService);
        }

    }

    @Nested
    class GetAllBySearch {

        @Test
        void shouldReturnPersonDtoPageWhenColorIsNotNullAndPersonsExist() {
            PersonDto personDto = mock(PersonDto.class);
            Pageable pageable = mock(Pageable.class);
            ColorDto color = ColorDto.BLUE;
            String search = "search";
            when(personService.retrievePersonsBySearch(search, color, pageable)).thenReturn(new PageImpl<>(List.of(personDto)));

            Page<PersonDto> actual = objectUnderTest.getAllBySearch(search, color, pageable);

            assertThat(actual).isNotNull();
            assertThat(actual.getContent()).hasSize(1);
            assertThat(actual.getContent().getFirst()).isEqualTo(personDto);
            verify(personService).retrievePersonsBySearch(search, color, pageable);
            verifyNoMoreInteractions(personService);
        }

        @Test
        void shouldReturnEmptyPageWhenColorIsNotNullAndPersonsNotExist() {
            Pageable pageable = mock(Pageable.class);
            ColorDto color = ColorDto.BLUE;
            String search = "search";
            when(personService.retrievePersonsBySearch(search, color, pageable)).thenReturn(new PageImpl<>(List.of()));

            Page<PersonDto> actual = objectUnderTest.getAllBySearch(search, color, pageable);

            assertThat(actual).isNotNull();
            assertThat(actual.getContent()).isEmpty();
            verify(personService).retrievePersonsBySearch(search, color, pageable);
            verifyNoMoreInteractions(personService);
        }

        @Test
        void shouldReturnPersonDtoPageWhenColorIsNullAndPersonsExist() {
            PersonDto personDto = mock(PersonDto.class);
            Pageable pageable = mock(Pageable.class);
            ColorDto color = null;
            String search = "search";
            when(personService.retrievePersonsBySearch(search, color, pageable)).thenReturn(new PageImpl<>(List.of(personDto)));

            Page<PersonDto> actual = objectUnderTest.getAllBySearch(search, color, pageable);

            assertThat(actual).isNotNull();
            assertThat(actual.getContent()).hasSize(1);
            assertThat(actual.getContent().getFirst()).isEqualTo(personDto);
            verify(personService).retrievePersonsBySearch(search, color, pageable);
            verifyNoMoreInteractions(personService);
        }

        @Test
        void shouldReturnEmptyPageWhenColorIsNullAndPersonsNotExist() {
            Pageable pageable = mock(Pageable.class);
            ColorDto color = null;
            String search = "search";
            when(personService.retrievePersonsBySearch(search, color, pageable)).thenReturn(new PageImpl<>(List.of()));

            Page<PersonDto> actual = objectUnderTest.getAllBySearch(search, color, pageable);

            assertThat(actual).isNotNull();
            assertThat(actual.getContent()).isEmpty();
            verify(personService).retrievePersonsBySearch(search, color, pageable);
            verifyNoMoreInteractions(personService);
        }

    }

    @Nested
    class GetById {

        @Test
        void shouldReturnPersonDtoResponseEntityWhenPersonExists() {
            long id = 1L;
            PersonDto personDto = mock(PersonDto.class);
            when(personService.retrievePersonById(id)).thenReturn(Optional.of(personDto));

            ResponseEntity<PersonDto> actual = objectUnderTest.getById(id);

            assertThat(actual).isNotNull();
            assertThat(actual.getBody()).isEqualTo(personDto);
            verify(personService).retrievePersonById(id);
            verifyNoMoreInteractions(personService);
        }

        @Test
        void shouldReturnEmptyResponseEntityWhenPersonNotExists() {
            long id = 1L;
            when(personService.retrievePersonById(id)).thenReturn(Optional.empty());

            ResponseEntity<PersonDto> actual = objectUnderTest.getById(id);

            assertThat(actual).isNotNull();
            assertThat(actual.getBody()).isNull();
            verify(personService).retrievePersonById(id);
            verifyNoMoreInteractions(personService);
        }

    }

    @Nested
    class Post {

        @Test
        void shouldReturnPersonDtoResponseEntity() {
            PersonSaveDto personSaveDto = mock(PersonSaveDto.class);
            PersonDto personDto = mock(PersonDto.class);
            when(personService.savePerson(personSaveDto)).thenReturn(personDto);
            long personId = 1L;
            when(personDto.getId()).thenReturn(personId);

            ResponseEntity<PersonDto> actual = objectUnderTest.post(personSaveDto);

            assertThat(actual).isNotNull();
            assertThat(actual.getBody()).isEqualTo(personDto);
            assertThat(actual.getHeaders().getLocation()).isNotNull();
            assertThat(actual.getHeaders().getLocation().getPath()).isEqualTo(PersonController.PATH + "/" + personId);
            verify(personService).savePerson(personSaveDto);
            verifyNoMoreInteractions(personService);
        }

    }

}
