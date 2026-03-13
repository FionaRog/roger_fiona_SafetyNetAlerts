package com.openclassrooms.safetynetalerts.controller.endpoint;

import com.openclassrooms.safetynetalerts.dto.FireResponseDTO;
import com.openclassrooms.safetynetalerts.service.endpoint.IFireService;

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

@WebMvcTest(FireController.class)
public class FireControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private IFireService fireService;

    @Test
    @DisplayName("Should return persons for address when parameter is provided")
    void shouldReturnPersonsWhenAddressParameterIsProvided() throws Exception {

        FireResponseDTO response = new FireResponseDTO();
        response.setStation("3");
        response.setFirePersonDtos(List.of());

        when(fireService.getPersonByAddress("1509 Culver St"))
                .thenReturn(response);

        mockMvc.perform(get("/fire")
                        .param("address", "1509 Culver St"))
                .andExpect(status().isOk());

        verify(fireService).getPersonByAddress("1509 Culver St");
    }

    @Test
    @DisplayName("Should return ok when address parameter is blank")
    void shouldReturnOkWhenAddressParameterIsBlank() throws Exception {

        FireResponseDTO response = new FireResponseDTO();
        response.setStation(null);
        response.setFirePersonDtos(List.of());

        when(fireService.getPersonByAddress("   "))
                .thenReturn(response);

        mockMvc.perform(get("/fire")
                        .param("address", "   "))
                .andExpect(status().isOk());

        verify(fireService).getPersonByAddress("   ");
    }
}
