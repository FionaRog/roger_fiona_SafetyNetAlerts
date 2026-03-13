package com.openclassrooms.safetynetalerts.controller.endpoint;

import com.openclassrooms.safetynetalerts.service.endpoint.IFloodService;

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

@WebMvcTest(FloodController.class)
public class FloodControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private IFloodService floodService;

    @Test
    @DisplayName("Should return households when stations parameter is provided")
    void shouldReturnHouseholdsWhenStationsParameterIsProvided() throws Exception {

        when(floodService.getHouseholdsByStations(List.of("1","2")))
                .thenReturn(List.of());

        mockMvc.perform(get("/flood/stations")
                        .param("stations", "1","2"))
                .andExpect(status().isOk());

        verify(floodService).getHouseholdsByStations(List.of("1","2"));
    }

    @Test
    @DisplayName("Should return bad request when stations parameter is missing")
    void shouldReturnBadRequestWhenStationsParameterIsMissing() throws Exception {

        mockMvc.perform(get("/flood/stations"))
                .andExpect(status().isBadRequest());
    }
}
