package com.openclassrooms.safetynetalerts.controller.endpoint;

import com.openclassrooms.safetynetalerts.service.endpoint.IPhoneAlertService;

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

@WebMvcTest(PhoneAlertController.class)
public class PhoneAlertControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private IPhoneAlertService phoneAlertService;

    @Test
    @DisplayName("Should return phone numbers when firestation parameter is provided")
    void shouldReturnPhoneNumbersWhenFirestationParameterIsProvided() throws Exception {

        when(phoneAlertService.getPhoneByFirestation("3"))
                .thenReturn(List.of("841-874-6512", "841-874-8547"));

        mockMvc.perform(get("/phoneAlert")
                        .param("firestation", "3"))
                .andExpect(status().isOk());

        verify(phoneAlertService).getPhoneByFirestation("3");
    }

    @Test
    @DisplayName("Should return ok when firestation parameter is blank")
    void shouldReturnOkWhenFirestationParameterIsBlank() throws Exception {

        when(phoneAlertService.getPhoneByFirestation("   "))
                .thenReturn(List.of());

        mockMvc.perform(get("/phoneAlert")
                        .param("firestation", "   "))
                .andExpect(status().isOk());

        verify(phoneAlertService).getPhoneByFirestation("   ");
    }
}
