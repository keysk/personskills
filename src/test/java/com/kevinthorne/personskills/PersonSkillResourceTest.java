package com.kevinthorne.personskills;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kevinthorne.personskills.model.Person;
import com.kevinthorne.personskills.model.PersonSkill;
import com.kevinthorne.personskills.model.PersonSkillKey;
import com.kevinthorne.personskills.model.Skill;
import com.kevinthorne.personskills.repository.PersonRepository;
import com.kevinthorne.personskills.repository.PersonSkillRepository;
import com.kevinthorne.personskills.repository.SkillRepository;
import com.kevinthorne.personskills.resource.PersonResource;
import com.kevinthorne.personskills.resource.PersonSkillResource;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PersonSkillResource.class)
public class PersonSkillResourceTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PersonRepository personRepository;

    @MockBean
    private PersonSkillRepository personSkillRepository;

    @MockBean
    private SkillRepository skillRepository;

    @MockBean
    private Person person;

    @Test
    public void getPersonSkillShouldReturn404NotFoundResponse() throws Exception {
        this.mockMvc.perform(get("/personskill/{id}", 12345)
                .accept("application/json"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void getPersonSkillShouldReturn202AcceptedResponse() throws Exception {

        Person person = new Person (1234L, "first", "surname");
        Optional<Person>  personReturn = Optional.ofNullable(person);
        PersonSkill personSkill = new PersonSkill(new PersonSkillKey(1234L, 5678L), PersonSkill.SkillLevelsEnum.AWARENESS);

        List personSkillList = new ArrayList();
        personSkillList.add(personSkill);
        when(personRepository.findById(1234L)).thenReturn(personReturn);
        when(personSkillRepository.findByIdPersonId(1234L)).thenReturn(personSkillList);

        this.mockMvc.perform(get("/personskills/{id}", 1234)
                .accept("application/json"))
                .andDo(print())
                .andExpect(status().isAccepted())
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].id.personId").value(1234))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].id.skillId").value(5678))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].skillLevel").value("Awareness"));
    }


    @Test
    public void createPersonSkillShouldReturn201CreatedResponse() throws Exception {

        Person person = new Person (1234L, "first", "surname");
        Optional<Person>  personReturn = Optional.ofNullable(person);
        when(personRepository.findById(1234L)).thenReturn(personReturn);

        Skill skill = new Skill (5678L, "skill1" );
        Optional<Skill>  skillReturn = Optional.ofNullable(skill);
        when(skillRepository.findById(5678L)).thenReturn(skillReturn);

        this.mockMvc.perform(post("/personskill")
                .content(asJsonString(new PersonSkill(new PersonSkillKey(1234L, 5678L),  PersonSkill.SkillLevelsEnum.AWARENESS)))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    public void updatePersonSkillShouldReturn404NotFoundResponse() throws Exception {
        this.mockMvc.perform(put("/personskill")
                .content(asJsonString(new PersonSkill(new PersonSkillKey(1234L, 5678L), PersonSkill.SkillLevelsEnum.AWARENESS)))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void updatePersonSkillShouldReturn202AcceptedResponse() throws Exception {
        PersonSkillKey personSkillKey = new PersonSkillKey(1234L, 5678L);
        PersonSkill personSkill = new PersonSkill (personSkillKey, PersonSkill.SkillLevelsEnum.AWARENESS);
        Optional<PersonSkill>  personSkillReturn = Optional.ofNullable(personSkill);
        when(personSkillRepository.findById(personSkillKey)).thenReturn(personSkillReturn);

        this.mockMvc.perform(put("/personskill")
                .content(asJsonString(new PersonSkill (personSkillKey, PersonSkill.SkillLevelsEnum.AWARENESS)))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted());
    }

    @Test
    public void deletePersonSkillShouldReturn404NotFoundResponse() throws Exception {
        this.mockMvc.perform(delete("/personskill/{personId}/{skillId}", 12345, 4567))
                .andExpect(status().isNotFound());
    }

    public static String asJsonString(final Object obj) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            return mapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
