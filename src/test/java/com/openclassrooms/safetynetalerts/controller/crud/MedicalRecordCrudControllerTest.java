package com.openclassrooms.safetynetalerts.controller.crud;

import com.openclassrooms.safetynetalerts.controller.crud.MedicalRecordCrudController;
import com.openclassrooms.safetynetalerts.model.MedicalRecord;
import com.openclassrooms.safetynetalerts.service.crud.IMedicalRecordCrudService;

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

@WebMvcTest(MedicalRecordCrudController.class)
public class MedicalRecordCrudControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private IMedicalRecordCrudService medicalRecordCrudService;

    @Test
    @DisplayName("Should return medical records")
    void shouldReturnMedicalRecords() throws Exception {

        when(medicalRecordCrudService.getMedicalRecord())
                .thenReturn(List.of(new MedicalRecord()));

        mockMvc.perform(get("/medicalRecord"))
                .andExpect(status().isOk());

        verify(medicalRecordCrudService).getMedicalRecord();
    }

    @Test
    @DisplayName("Should create medical record")
    void shouldCreateMedicalRecord() throws Exception {

        when(medicalRecordCrudService.addMedicalRecord(any(MedicalRecord.class)))
                .thenReturn(true);

        mockMvc.perform(post("/medicalRecord")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                          "firstName": "John",
                          "lastName": "Boyd",
                          "birthdate": "01/01/1980",
                          "medications": [],
                          "allergies": []
                        }
                        """))
                .andExpect(status().isCreated());

        verify(medicalRecordCrudService).addMedicalRecord(any(MedicalRecord.class));
    }

    @Test
    @DisplayName("Should update medical record")
    void shouldUpdateMedicalRecord() throws Exception {

        when(medicalRecordCrudService.updateMedicalRecord(any(MedicalRecord.class)))
                .thenReturn(true);

        mockMvc.perform(put("/medicalRecord")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                          "firstName": "John",
                          "lastName": "Boyd",
                          "birthdate": "01/01/1981",
                          "medications": [],
                          "allergies": []
                        }
                        """))
                .andExpect(status().isNoContent());

        verify(medicalRecordCrudService).updateMedicalRecord(any(MedicalRecord.class));
    }

    @Test
    @DisplayName("Should delete medical record")
    void shouldDeleteMedicalRecord() throws Exception {

        when(medicalRecordCrudService.deleteMedicalRecord("John", "Boyd"))
                .thenReturn(true);

        mockMvc.perform(delete("/medicalRecord")
                        .param("firstName", "John")
                        .param("lastName", "Boyd"))
                .andExpect(status().isNoContent());

        verify(medicalRecordCrudService).deleteMedicalRecord("John", "Boyd");
    }
}
