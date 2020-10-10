package com.kevinthorne.personskills.resource;

import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kevinthorne.personskills.model.ApiError;
import com.kevinthorne.personskills.model.Person;
import com.kevinthorne.personskills.model.PersonSkill;
import com.kevinthorne.personskills.repository.PersonRepository;
import com.kevinthorne.personskills.repository.PersonSkillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class PersonResource {

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private PersonSkillRepository personSkillRepository;

    private final ObjectMapper objectMapper;

    public PersonResource(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @RequestMapping(value = "/person/{id}",
            produces = {MediaType.APPLICATION_JSON_VALUE},
            method = RequestMethod.GET)
    public ResponseEntity<Object> retrievePerson(@PathVariable long id) throws JsonProcessingException {
        Optional<Person> Person = personRepository.findById(id);

        if (!Person.isPresent())
            return createApiErrorResponse(HttpStatus.NOT_FOUND, "Person " + id + " Not Found");

        return new ResponseEntity(objectMapper.writeValueAsString(Person.get()), HttpStatus.ACCEPTED);
    }

    @RequestMapping(value = "/person/{id}",
            produces = {MediaType.APPLICATION_JSON_VALUE},
            method = RequestMethod.DELETE)
    public ResponseEntity<Object>  deletePerson(@PathVariable long id) throws JsonProcessingException {
        Optional<Person> Person = personRepository.findById(id);

        if (!Person.isPresent())
            return createApiErrorResponse(HttpStatus.NOT_FOUND, "Person " + id + " Not Found");

        // delete all skills associated with Person
        List<PersonSkill> PersonSkillList = personSkillRepository.findByIdPersonId(id);
        for(PersonSkill PersonSkill : PersonSkillList) {
            personSkillRepository.deleteById(PersonSkill.getId());
        }

        try {
            personRepository.deleteById(id);
        }
        catch (Exception e){
            return createApiErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage());
        }

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/person")
    public ResponseEntity<Object> createPerson(@Valid @RequestBody Person Person) throws JsonProcessingException {

        try {
            Person savedPerson = personRepository.save(Person);
            return new ResponseEntity<Object>(Person, HttpStatus.CREATED);
        }
        catch (Exception e){
            return createApiErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @RequestMapping(value = "/person/{id}",
            produces = {MediaType.APPLICATION_JSON_VALUE},
            method = RequestMethod.PUT)
    public ResponseEntity<Object> updatePerson(@Valid @RequestBody Person Person, @PathVariable long id) throws JsonProcessingException {

        Optional<Person> PersonOptional = personRepository.findById(id);

        if (!PersonOptional.isPresent())
            return createApiErrorResponse(HttpStatus.NOT_FOUND, "Person " + id + " Not Found");

        Person.setId(id);

        try {
            personRepository.save(Person);
        }
        catch (Exception e){
            createApiErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage());
        }

        return ResponseEntity.accepted().build();
    }

    private ResponseEntity createApiErrorResponse(final HttpStatus status, final String msg) throws JsonProcessingException {
        final ApiError apiError = new ApiError(status, msg);
        return new ResponseEntity<>(objectMapper.writeValueAsString(apiError), status);
    }
}

