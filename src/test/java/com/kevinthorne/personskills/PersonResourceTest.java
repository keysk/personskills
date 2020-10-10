package com.kevinthorne.personskills;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kevinthorne.personskills.model.Person;
import com.kevinthorne.personskills.repository.PersonRepository;
import com.kevinthorne.personskills.repository.PersonSkillRepository;
import com.kevinthorne.personskills.resource.PersonResource;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PersonResource.class)
public class PersonResourceTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PersonRepository personRepository;

    @MockBean
    private PersonSkillRepository personSkillRepository;

    @MockBean
    private Person person;

    @Test
    public void getPersonShouldReturn404NotFoundResponse() throws Exception {
        this.mockMvc.perform(get("/person/{id}", 12345)
                .accept("application/json"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void getPersonShouldReturn202AcceptedResponse() throws Exception {
        Person person = new Person (12345L, "Kevin", "Thorne");
        Optional<Person>  personReturn = Optional.ofNullable(person);
        when(personRepository.findById(12345L)).thenReturn(personReturn);

        this.mockMvc.perform(get("/person/{id}", 12345)
                .accept("application/json"))
                .andDo(print())
                .andExpect(status().isAccepted())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(12345))
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value("Kevin"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.surname").value("Thorne"));
    }


    @Test
    public void createPersonShouldReturn201CreatedResponse() throws Exception {
        this.mockMvc.perform(post("/person")
                .content(asJsonString(new Person(null, "firstName", "surname")))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value("firstName"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.surname").value("surname"));
    }

    @Test
    public void updatePersonShouldReturn404NotFoundResponse() throws Exception {
        this.mockMvc.perform(put("/person/{id}", 12345)
                .content(asJsonString(new Person(12345L, "firstName", "surname")))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void updatePersonShouldReturn202AcceptedResponse() throws Exception {
        Person person = new Person (12345L, "Kevin", "Thorne");
        Optional<Person>  personReturn = Optional.ofNullable(person);
        when(personRepository.findById(12345L)).thenReturn(personReturn);

        this.mockMvc.perform(put("/person/{id}", 12345)
                .content(asJsonString(new Person(12345L, "firstName", "surname")))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted());
    }

    @Test
    public void deletePersonShouldReturn404NotFoundResponse() throws Exception {
        this.mockMvc.perform(delete("/person/{id}", 12345))
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
