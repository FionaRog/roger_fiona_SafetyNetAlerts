package com.openclassrooms.safetynetalerts.controller.endpoint;
import com.openclassrooms.safetynetalerts.service.endpoint.ICommunityEmailService;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CommunityEmailController.class)
public class CommunityEmailControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ICommunityEmailService communityEmailService;

    @Test
    @DisplayName("Should return emails when city parameter is provided")
    void shouldReturnEmailsForCity() throws Exception {

        when(communityEmailService.getEmailsByCity("Culver"))
                .thenReturn(List.of("john@email.com", "jacob@email.com"));

        mockMvc.perform(get("/communityEmail")
                        .param("city", "Culver"))
                .andExpect(status().isOk());

        verify(communityEmailService).getEmailsByCity("Culver");
    }

    @Test
    @DisplayName("Should return ok when city parameter is blank")
    void shouldReturnOkWhenCityParameterIsBlank() throws Exception {

        when(communityEmailService.getEmailsByCity("   "))
                .thenReturn(List.of());

        mockMvc.perform(get("/communityEmail")
                        .param("city", "   "))
                .andExpect(status().isOk());

        verify(communityEmailService).getEmailsByCity("   ");
    }
}
