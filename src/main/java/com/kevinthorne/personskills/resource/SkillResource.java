package com.kevinthorne.personskills.resource;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kevinthorne.personskills.model.ApiError;
import com.kevinthorne.personskills.model.PersonSkill;
import com.kevinthorne.personskills.model.Skill;
import com.kevinthorne.personskills.repository.PersonSkillRepository;
import com.kevinthorne.personskills.repository.SkillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
public class SkillResource {

    @Autowired
    private SkillRepository skillRepository;

    @Autowired
    private PersonSkillRepository personSkillRepository;

    private final ObjectMapper objectMapper;

    public SkillResource(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @GetMapping("/skills")
    public List<Skill> retrieveAllSkills() {
            return skillRepository.findAll();
        }

    @RequestMapping(value = "/skill/{id}",
            produces = {MediaType.APPLICATION_JSON_VALUE},
            method = RequestMethod.GET)
    public ResponseEntity<Object> retrieveSkill(@PathVariable long id) throws JsonProcessingException {
        Optional<Skill> Skill = skillRepository.findById(id);

        if (!Skill.isPresent())
            return createApiErrorResponse(HttpStatus.NOT_FOUND, "Skill " + id + " Not Found");

        return new ResponseEntity(objectMapper.writeValueAsString(Skill.get()), HttpStatus.ACCEPTED);
    }

    @RequestMapping(value = "/skill/{id}",
            produces = {MediaType.APPLICATION_JSON_VALUE},
            method = RequestMethod.DELETE)
    public ResponseEntity<Object>  deleteSkill(@PathVariable long id) throws JsonProcessingException {

        Optional<Skill> SkillOptional = skillRepository.findById(id);

        if (!SkillOptional.isPresent())
            return createApiErrorResponse(HttpStatus.NOT_FOUND, "Skill " + id + " Not Found");

        // don't allow deletion if skills associated with a Person
        List<PersonSkill> PersonSkillList = personSkillRepository.findByIdPersonId(id);
        if(!PersonSkillList.isEmpty())
            return createApiErrorResponse(HttpStatus.BAD_REQUEST, "Unable to delete as Skill " + id + " as it is assigned to a Person");

        try {
            skillRepository.deleteById(id);
        }
        catch (Exception e){
            return createApiErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage());
        }

        return ResponseEntity.noContent().build();

    }

    @PostMapping("/skill")
    public ResponseEntity<Object> createSkill(@Valid @RequestBody Skill Skill) {
        Skill savedSkill = skillRepository.save(Skill);

        return new ResponseEntity<Object>(Skill, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/skill/{id}",
            produces = {MediaType.APPLICATION_JSON_VALUE},
            method = RequestMethod.PUT)
    public ResponseEntity<Object> updateSkill(@Valid  @RequestBody Skill Skill, @PathVariable long id) throws JsonProcessingException{

            Optional<Skill> SkillOptional = skillRepository.findById(id);

            if (!SkillOptional.isPresent())
                return createApiErrorResponse(HttpStatus.NOT_FOUND, "Skill " + id + " Not Found");

            Skill.setId(id);

            try {
                skillRepository.save(Skill);
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

