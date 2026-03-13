package com.openclassrooms.safetynetalerts.controller.endpoint;

import com.openclassrooms.safetynetalerts.service.endpoint.IPersonInfoService;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PersonInfoController.class)
public class PersonInfoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private IPersonInfoService personInfoService;

    @Test
    @DisplayName("Should return persons when lastName parameter is provided")
    void shouldReturnPersonsWhenLastNameParameterIsProvided() throws Exception {

        when(personInfoService.getPersonsByLastName("Boyd"))
                .thenReturn(List.of());

        mockMvc.perform(get("/personInfo")
                        .param("lastName", "Boyd"))
                .andExpect(status().isOk());

        verify(personInfoService).getPersonsByLastName("Boyd");
    }

    @Test
    @DisplayName("Should return bad request when lastName parameter is missing")
    void shouldReturnBadRequestWhenLastNameParameterIsMissing() throws Exception {

        mockMvc.perform(get("/personInfo"))
                .andExpect(status().isBadRequest());
    }
}
