package com.openclassrooms.safetynetalerts.controller.endpoint;

import com.openclassrooms.safetynetalerts.service.endpoint.IChildAlertService;

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

@WebMvcTest(ChildAlertController.class)
public class ChildAlertControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private IChildAlertService childAlertService;

    @Test
    @DisplayName("Should return children when address parameter is provided")
    void shouldReturnChildrenWhenAddressParameterIsProvided() throws Exception {

        when(childAlertService.getChildByAddress("1509 Culver St"))
                .thenReturn(List.of());

        mockMvc.perform(get("/childAlert")
                        .param("address", "1509 Culver St"))
                .andExpect(status().isOk());

        verify(childAlertService).getChildByAddress("1509 Culver St");
    }

    @Test
    @DisplayName("Should return ok when address parameter is blank")
    void shouldReturnOkWhenAddressParameterIsBlank() throws Exception {

        when(childAlertService.getChildByAddress("   "))
                .thenReturn(List.of());

        mockMvc.perform(get("/childAlert")
                        .param("address", "   "))
                .andExpect(status().isOk());

        verify(childAlertService).getChildByAddress("   ");
    }

}
