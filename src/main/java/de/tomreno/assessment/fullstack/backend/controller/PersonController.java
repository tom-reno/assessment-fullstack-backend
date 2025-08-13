package de.tomreno.assessment.fullstack.backend.controller;

import de.tomreno.assessment.fullstack.backend.domain.ColorDto;
import de.tomreno.assessment.fullstack.backend.domain.person.PersonDto;
import de.tomreno.assessment.fullstack.backend.domain.person.PersonSaveDto;
import de.tomreno.assessment.fullstack.backend.service.PersonService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping(PersonController.PATH)
public class PersonController {

    static final String PATH = "persons";

    private final PersonService personService;

    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @GetMapping(params = {"!search"})
    Page<PersonDto> getAll(@PageableDefault Pageable pageable) {
        return personService.retrieveAllPersons(pageable);
    }

    @GetMapping("color/{color}")
    Page<PersonDto> getAllByColor(@PathVariable ColorDto color, @PageableDefault Pageable pageable) {
        return personService.retrievePersonsByColor(color, pageable);
    }

    @GetMapping(params = {"search"})
    Page<PersonDto> getAllBySearch(
            @RequestParam("search") String search,
            @RequestParam(name = "color", required = false) ColorDto color,
            @PageableDefault Pageable pageable
    ) {
        return personService.retrievePersonsBySearch(search, color, pageable);
    }

    @GetMapping("{id}")
    ResponseEntity<PersonDto> getById(@PathVariable long id) {
        return ResponseEntity.of(personService.retrievePersonById(id));
    }

    @PostMapping
    ResponseEntity<PersonDto> post(@RequestBody @Valid PersonSaveDto personSaveDto) {
        PersonDto savedPerson = personService.savePerson(personSaveDto);
        URI location = URI.create(PATH + "/" + savedPerson.getId());
        return ResponseEntity.created(location).body(savedPerson);
    }

}
