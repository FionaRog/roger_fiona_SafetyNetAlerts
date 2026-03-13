package com.openclassrooms.safetynetalerts.controller.endpoint;

import com.openclassrooms.safetynetalerts.dto.FirestationResponseDTO;
import com.openclassrooms.safetynetalerts.service.IFirestationService;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FirestationController.class)
public class FirestationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private IFirestationService firestationService;

    @Test
    @DisplayName("Should return persons when stationNumber parameter is provided")
    void shouldReturnPersonsWhenStationNumberParameterIsProvided() throws Exception {

        FirestationResponseDTO response = new FirestationResponseDTO();

        when(firestationService.getPersonsByFirestation("3"))
                .thenReturn(response);

        mockMvc.perform(get("/firestation")
                        .param("stationNumber", "3"))
                .andExpect(status().isOk());

        verify(firestationService).getPersonsByFirestation("3");
    }

    @Test
    @DisplayName("Should return bad request when stationNumber parameter is missing")
    void shouldReturnBadRequestWhenStationNumberParameterIsMissing() throws Exception {

        mockMvc.perform(get("/firestation"))
                .andExpect(status().isBadRequest());
    }
}
