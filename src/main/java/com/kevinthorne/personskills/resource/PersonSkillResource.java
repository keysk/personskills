package com.kevinthorne.personskills.resource;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kevinthorne.personskills.model.ApiError;
import com.kevinthorne.personskills.model.*;
import com.kevinthorne.personskills.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
public class PersonSkillResource {

    @Autowired
    private PersonSkillRepository personSkillRepository;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private SkillRepository skillRepository;

    private final ObjectMapper objectMapper;

    public PersonSkillResource(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @RequestMapping(value = "/personskills/{personId}",
            produces = {MediaType.APPLICATION_JSON_VALUE},
            method = RequestMethod.GET)
    public ResponseEntity<Object> getPersonSkillsForPerson(@PathVariable long personId) throws JsonProcessingException {

        Optional<Person> Person = personRepository.findById(personId);

        if (!Person.isPresent())
            return createApiErrorResponse(HttpStatus.NOT_FOUND, "Person " + personId + " Not Found");

        return new ResponseEntity(objectMapper.writeValueAsString(personSkillRepository.findByIdPersonId(personId)), HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/personskill/{personId}/{skillId}")
    public ResponseEntity<Object> deletePersonSkill(@PathVariable long personId, @PathVariable long skillId) throws JsonProcessingException {

        PersonSkillKey PersonSkillKey = new PersonSkillKey(personId, skillId);

        Optional<PersonSkill> PersonSkill = personSkillRepository.findById(PersonSkillKey);

        if (!PersonSkill.isPresent())
            return createApiErrorResponse(HttpStatus.NOT_FOUND, "Skill id " + skillId + " for Person " + personId + " Not Found");

        try {
            personSkillRepository.deleteById(PersonSkillKey);
        }
        catch (Exception e){
            return createApiErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage());
        }

        return ResponseEntity.noContent().build();
    }

    @RequestMapping(value = "/personskill",
            produces = {MediaType.APPLICATION_JSON_VALUE},
            method = RequestMethod.POST)
    public ResponseEntity<Object> createPerson(@Valid @RequestBody PersonSkill PersonSkill) throws JsonProcessingException {

        Optional<Person> Person = personRepository.findById(PersonSkill.getId().getPersonId());
        if (!Person.isPresent())
            return createApiErrorResponse(HttpStatus.NOT_FOUND, "Person " + PersonSkill.getId().getPersonId() + " Not Found");

        Optional<Skill> SkillOptional = skillRepository.findById(PersonSkill.getId().getSkillId());
        if (!SkillOptional.isPresent())
            return createApiErrorResponse(HttpStatus.NOT_FOUND, "Skill " + PersonSkill.getId().getSkillId() + " Not Found");

        Optional<PersonSkill> PersonSkillOptional = personSkillRepository.findById(PersonSkill.getId());
        if (PersonSkillOptional.isPresent())
            return createApiErrorResponse(HttpStatus.CONFLICT, "Person Skill Already Exsits");

        try {
            PersonSkill savedPersonSkill = personSkillRepository.save(PersonSkill);
            return new ResponseEntity<Object>(savedPersonSkill, HttpStatus.CREATED);
        }
        catch (Exception e){
            return createApiErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage());
        }


    }

    @RequestMapping(value = "/personskill",
            produces = {MediaType.APPLICATION_JSON_VALUE},
            method = RequestMethod.PUT)
    public ResponseEntity<Object> updatePerson(@Valid @RequestBody PersonSkill PersonSkill) throws JsonProcessingException {

        Optional<PersonSkill> PersonSkillOptional = personSkillRepository.findById(PersonSkill.getId());

        if (!PersonSkillOptional.isPresent())
            return createApiErrorResponse(HttpStatus.NOT_FOUND, "Person Skill Not Found");

        PersonSkill.setId(PersonSkill.getId());

        try {
            personSkillRepository.save(PersonSkill);
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

