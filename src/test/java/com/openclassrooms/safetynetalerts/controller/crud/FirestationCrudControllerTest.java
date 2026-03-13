package com.openclassrooms.safetynetalerts.controller.crud;

import com.openclassrooms.safetynetalerts.controller.crud.FirestationCrudController;
import com.openclassrooms.safetynetalerts.model.Firestation;
import com.openclassrooms.safetynetalerts.service.IFirestationService;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FirestationCrudController.class)
public class FirestationCrudControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private IFirestationService firestationService;

    @Test
    @DisplayName("Should return mappings when stationNumber parameter is not provided")
    void shouldReturnMappingsWhenStationNumberParameterIsNotProvided() throws Exception {

        when(firestationService.getFirestation()).thenReturn(List.of(new Firestation()));

        mockMvc.perform(get("/firestation"))
                .andExpect(status().isOk());

        verify(firestationService).getFirestation();
    }

    @Test
    @DisplayName("Should create firestation mapping")
    void shouldCreateFirestationMapping() throws Exception {

        when(firestationService.addFirestation(org.mockito.ArgumentMatchers.any(Firestation.class)))
                .thenReturn(true);

        mockMvc.perform(post("/firestation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                          "address": "1509 Culver St",
                          "station": "3"
                        }
                        """))
                .andExpect(status().isCreated());

        verify(firestationService).addFirestation(org.mockito.ArgumentMatchers.any(Firestation.class));
    }

    @Test
    @DisplayName("Should update firestation mapping")
    void shouldUpdateFirestationMapping() throws Exception {

        when(firestationService.updateFirestationByAddress(org.mockito.ArgumentMatchers.any(Firestation.class)))
                .thenReturn(true);

        mockMvc.perform(put("/firestation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                          "address": "1509 Culver St",
                          "station": "4"
                        }
                        """))
                .andExpect(status().isNoContent());

        verify(firestationService).updateFirestationByAddress(org.mockito.ArgumentMatchers.any(Firestation.class));
    }

    @Test
    @DisplayName("Should delete firestation mapping by address")
    void shouldDeleteFirestationMappingByAddress() throws Exception {

        when(firestationService.deleteFirestationByAddress("1509 Culver St"))
                .thenReturn(true);

        mockMvc.perform(delete("/firestation")
                        .param("address", "1509 Culver St"))
                .andExpect(status().isNoContent());

        verify(firestationService).deleteFirestationByAddress("1509 Culver St");
    }

    @Test
    @DisplayName("Should delete firestation mappings by station")
    void shouldDeleteFirestationMappingsByStation() throws Exception {

        when(firestationService.deleteFirestationByStation("3"))
                .thenReturn(true);

        mockMvc.perform(delete("/firestation")
                        .param("station", "3"))
                .andExpect(status().isNoContent());

        verify(firestationService).deleteFirestationByStation("3");
    }
}
