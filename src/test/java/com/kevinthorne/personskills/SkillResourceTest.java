package com.kevinthorne.personskills;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kevinthorne.personskills.model.Skill;
import com.kevinthorne.personskills.repository.PersonSkillRepository;
import com.kevinthorne.personskills.repository.SkillRepository;
import com.kevinthorne.personskills.resource.SkillResource;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SkillResource.class)
public class SkillResourceTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SkillRepository skillRepository;

    @MockBean
    private PersonSkillRepository personSkillRepository;

    @MockBean
    private Skill skill;

    @Test
    public void getSkillShouldReturn404NotFoundResponse() throws Exception {
        this.mockMvc.perform(get("/skill/{id}", 12345)
                .accept("application/json"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void getSkillShouldReturn202AcceptedResponse() throws Exception {
        Skill skill = new Skill (1234L, "Skill1");
        Optional<Skill>  skillReturn = Optional.ofNullable(skill);
        when(skillRepository.findById(1234L)).thenReturn(skillReturn);

        this.mockMvc.perform(get("/skill/{id}", 1234L)
                .accept("application/json"))
                .andDo(print())
                .andExpect(status().isAccepted())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1234L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Skill1"));
    }


    @Test
    public void createSkillShouldReturn201CreatedResponse() throws Exception {
        this.mockMvc.perform(post("/skill")
                .content(asJsonString(new Skill(null, "Skill2")))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Skill2"));
    }

    @Test
    public void updateSkillShouldReturn404NotFoundResponse() throws Exception {
        this.mockMvc.perform(put("/skill/{id}", 1234)
                .content(asJsonString(new Skill(1234L, "Skill1")))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void updateSkillShouldReturn202AcceptedResponse() throws Exception {
        Skill skill = new Skill (1234L, "SkillUpdate");
        Optional<Skill>  personReturn = Optional.ofNullable(skill);
        when(skillRepository.findById(12345L)).thenReturn(personReturn);

        this.mockMvc.perform(put("/skill/{id}", 12345)
                .content(asJsonString(new Skill(1234L, "SkillUpdateNew")))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted());
    }

    @Test
    public void deleteSkillShouldReturn404NotFoundResponse() throws Exception {
        this.mockMvc.perform(delete("/skill/{id}", 12345))
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
