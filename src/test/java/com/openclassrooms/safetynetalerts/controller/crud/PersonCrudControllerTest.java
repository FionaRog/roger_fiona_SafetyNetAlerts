package com.openclassrooms.safetynetalerts.controller.crud;

import com.openclassrooms.safetynetalerts.model.Person;
import com.openclassrooms.safetynetalerts.service.crud.IPersonCrudService;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PersonCrudController.class)
public class PersonCrudControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private IPersonCrudService personCrudService;

    @Test
    @DisplayName("Should return persons")
    void shouldReturnPersons() throws Exception {

        when(personCrudService.getPerson()).thenReturn(List.of(new Person()));

        mockMvc.perform(get("/person"))
                .andExpect(status().isOk());

        verify(personCrudService).getPerson();
    }

    @Test
    @DisplayName("Should create person")
    void shouldCreatePerson() throws Exception {

        when(personCrudService.addPerson(any(Person.class))).thenReturn(true);

        mockMvc.perform(post("/person")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                          "firstName": "John",
                          "lastName": "Boyd",
                          "address": "1509 Culver St",
                          "city": "Culver",
                          "zip": "97451",
                          "phone": "841-874-6512",
                          "email": "john@email.com"
                        }
                        """))
                .andExpect(status().isCreated());

        verify(personCrudService).addPerson(any(Person.class));
    }

    @Test
    @DisplayName("Should update person")
    void shouldUpdatePerson() throws Exception {

        when(personCrudService.updatePerson(any(Person.class))).thenReturn(true);

        mockMvc.perform(put("/person")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                          "firstName": "John",
                          "lastName": "Boyd",
                          "address": "29 15th St",
                          "city": "Culver",
                          "zip": "97451",
                          "phone": "841-874-6512",
                          "email": "john@email.com"
                        }
                        """))
                .andExpect(status().isNoContent());

        verify(personCrudService).updatePerson(any(Person.class));
    }

    @Test
    @DisplayName("Should delete person")
    void shouldDeletePerson() throws Exception {

        when(personCrudService.deletePerson("John", "Boyd")).thenReturn(true);

        mockMvc.perform(delete("/person")
                        .param("firstName", "John")
                        .param("lastName", "Boyd"))
                .andExpect(status().isNoContent());

        verify(personCrudService).deletePerson("John", "Boyd");
    }
}
