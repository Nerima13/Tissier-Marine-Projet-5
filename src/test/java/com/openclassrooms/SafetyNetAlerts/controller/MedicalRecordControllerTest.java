package com.openclassrooms.SafetyNetAlerts.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.SafetyNetAlerts.model.MedicalRecord;
import com.openclassrooms.SafetyNetAlerts.service.impl.MedicalRecordServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MedicalRecordController.class)
public class MedicalRecordControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MedicalRecordServiceImpl medicalRecordService;

    @Test
    public void createMedicalRecordTest() throws Exception {
        MedicalRecord mr = new MedicalRecord();
        mr.setFirstName("John");
        mr.setLastName("Boyd");
        mr.setBirthdate("03/06/1984");
        mr.setMedications(List.of("aznol:350mg", "hydrapermazol:100mg"));
        mr.setAllergies(List.of("nillacilan"));

        mockMvc.perform(post("/medicalRecord")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mr)))
                .andExpect(status().isCreated());

        verify(medicalRecordService).add(mr);
    }

    @Test
    void createMedicalRecord_failure_throws() throws Exception {
        MedicalRecord mr = new MedicalRecord();
        mr.setFirstName("John");
        mr.setLastName("Boyd");
        mr.setBirthdate("03/06/1984");
        mr.setMedications(List.of("aznol:350mg", "hydrapermazol:100mg"));
        mr.setAllergies(List.of("nillacilan"));

        doThrow(new RuntimeException("Error encountered"))
                .when(medicalRecordService).add(any(MedicalRecord.class));

        assertThrows(Exception.class, () ->
                mockMvc.perform(post("/medicalRecord")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mr))));

        verify(medicalRecordService).add(mr);
    }

    @Test
    public void deleteMedicalRecordTest() throws Exception {
        MedicalRecord mr = new MedicalRecord();
        mr.setFirstName("John");
        mr.setLastName("Boyd");

        mockMvc.perform(delete("/medicalRecord")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mr)))
                .andExpect(status().isOk());

        verify(medicalRecordService).delete(any(MedicalRecord.class));
    }

    @Test
    void deleteMedicalRecord_failure_throws() throws Exception {
        MedicalRecord mr = new MedicalRecord();
        mr.setFirstName("John");
        mr.setLastName("Boyd");

        doThrow(new RuntimeException("Error encountered"))
                .when(medicalRecordService).delete(any(MedicalRecord.class));

        assertThrows(Exception.class, () ->
                mockMvc.perform(delete("/medicalRecord")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mr))));

        verify(medicalRecordService).delete(any(MedicalRecord.class));
    }

    @Test
    public void updateMedicalRecordTest() throws Exception {
        MedicalRecord mr = new MedicalRecord();
        mr.setFirstName("John");
        mr.setLastName("Boyd");
        mr.setBirthdate("03/06/1984");
        mr.setMedications(List.of("aznol:350mg", "hydrapermazol:100mg"));
        mr.setAllergies(List.of("nillacilan"));

        mockMvc.perform(put("/medicalRecord")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mr)))
                .andExpect(status().isOk());

        verify(medicalRecordService).update(any(MedicalRecord.class));
    }

    @Test
    void updateMedicalRecord_failure_throws() throws Exception {
        MedicalRecord mr = new MedicalRecord();
        mr.setFirstName("John");
        mr.setLastName("Boyd");
        mr.setBirthdate("03/06/1984");
        mr.setMedications(List.of("aznol:350mg", "hydrapermazol:100mg"));
        mr.setAllergies(List.of("nillacilan"));

        doThrow(new RuntimeException("Error encountered"))
                .when(medicalRecordService).update(any(MedicalRecord.class));

        assertThrows(Exception.class, () ->
                mockMvc.perform(put("/medicalRecord")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mr))));

        verify(medicalRecordService).update(any(MedicalRecord.class));
    }

    @Test
    public void getMedicalRecordTest() throws Exception {
        MedicalRecord mr = new MedicalRecord();
        mr.setFirstName("John");
        mr.setLastName("Boyd");
        mr.setBirthdate("03/06/1984");
        mr.setMedications(List.of("aznol:350mg", "hydrapermazol:100mg"));
        mr.setAllergies(List.of("nillacilan"));

        when(medicalRecordService.get(any(MedicalRecord.class))).thenReturn(mr);

        mockMvc.perform(get("/medicalRecord?firstName=John&lastName=Boyd"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Boyd"))
                .andExpect(jsonPath("$.medications", hasSize(2)))
                .andExpect(jsonPath("$.medications[0]").value("aznol:350mg"))
                .andExpect(jsonPath("$.medications[1]").value("hydrapermazol:100mg"))
                .andExpect(jsonPath("$.allergies[0]").value("nillacilan"));

        verify(medicalRecordService).get(any(MedicalRecord.class));
    }

    @Test
    void getMedicalRecord_failure_throws() throws Exception {
        when(medicalRecordService.get(any(MedicalRecord.class)))
                .thenThrow(new RuntimeException("Error encountered"));

        assertThrows(Exception.class, () ->
                mockMvc.perform(get("/medicalRecord?firstName=John&lastName=Boyd")));

        verify(medicalRecordService).get(any(MedicalRecord.class));
    }

    @Test
    public void getMedicalRecordListTest() throws Exception {
        MedicalRecord mr1 = new MedicalRecord();
        mr1.setFirstName("John");
        mr1.setLastName("Boyd");
        mr1.setBirthdate("03/06/1984");
        mr1.setMedications(List.of("aznol:350mg", "hydrapermazol:100mg"));
        mr1.setAllergies(List.of("nillacilan"));

        MedicalRecord mr2 = new MedicalRecord();
        mr2.setFirstName("Jacob");
        mr2.setLastName("Boyd");
        mr2.setBirthdate("03/06/1989");
        mr2.setMedications(List.of("pharmacol:5000mg", "terazine:10mg", "noznazol:250mg"));
        mr2.setAllergies(List.of());

        when(medicalRecordService.findAll()).thenReturn(List.of(mr1, mr2));

        mockMvc.perform(get("/medicalRecords"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].firstName").value("John"))
                .andExpect(jsonPath("$[1].firstName").value("Jacob"))
                .andExpect(jsonPath("$[0].medications", hasSize(2)))
                .andExpect(jsonPath("$[1].medications", hasSize(3)));

        verify(medicalRecordService).findAll();
    }

    @Test
    void getMedicalRecordList_failure_throws() throws Exception {
        when(medicalRecordService.findAll())
                .thenThrow(new RuntimeException("Error encountered"));

        assertThrows(Exception.class, () ->
                mockMvc.perform(get("/medicalRecords")));

        verify(medicalRecordService).findAll();
    }
}
