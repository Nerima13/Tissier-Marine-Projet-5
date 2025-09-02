package com.openclassrooms.SafetyNetAlerts.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.SafetyNetAlerts.model.FireStation;
import com.openclassrooms.SafetyNetAlerts.service.impl.FireStationServiceImpl;
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

@WebMvcTest(FireStationController.class)
public class FireStationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private FireStationServiceImpl fireStationService;

    @Test
    public void createFireStationTest() throws Exception {
        FireStation fs = new FireStation();
        fs.setAddress("1509 Culver St");
        fs.setStation("3");

        mockMvc.perform(post("/firestation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(fs)))
                .andExpect(status().isCreated());

        verify(fireStationService).add(fs);
    }

    @Test
    void createFireStation_failure_throws() throws Exception {
        FireStation fs = new FireStation();
        fs.setAddress("1509 Culver St");
        fs.setStation("3");

        doThrow(new RuntimeException("Error encountered"))
                .when(fireStationService).add(any(FireStation.class));

        assertThrows(Exception.class, () ->
                mockMvc.perform(post("/firestation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(fs))));

        verify(fireStationService).add(fs);
    }

    @Test
    public void deleteFireStationTest() throws Exception {
        FireStation fs = new FireStation();
        fs.setAddress("1509 Culver St");
        fs.setStation("3");

        mockMvc.perform(delete("/firestation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(fs)))
                .andExpect(status().isOk());

        verify(fireStationService).delete(fs);
    }

    @Test
    void deleteFireStation_failure_throws() throws Exception {
        FireStation fs = new FireStation();
        fs.setAddress("1509 Culver St");
        fs.setStation("3");

        doThrow(new RuntimeException("Error encountered"))
                .when(fireStationService).delete(any(FireStation.class));

        assertThrows(Exception.class, () ->
                mockMvc.perform(delete("/firestation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(fs))));

        verify(fireStationService).delete(fs);
    }

    @Test
    public void updateFireStationTest() throws Exception {
        FireStation fs = new FireStation();
        fs.setAddress("1509 Culver St");
        fs.setStation("3");

        mockMvc.perform(put("/firestation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(fs)))
                .andExpect(status().isOk());

        verify(fireStationService).update(fs);
    }

    @Test
    void updateFireStation_failure_throws() throws Exception {
        FireStation fs = new FireStation();
        fs.setAddress("1509 Culver St");
        fs.setStation("3");

        doThrow(new RuntimeException("Error encountered"))
                .when(fireStationService).update(any(FireStation.class));

        assertThrows(Exception.class, () ->
                mockMvc.perform(put("/firestation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(fs))));

        verify(fireStationService).update(fs);
    }

    @Test
    public void getFireStationTest() throws Exception {
        FireStation fs = new FireStation();
        fs.setAddress("1509 Culver St");
        fs.setStation("3");

        when(fireStationService.get(any(FireStation.class))).thenReturn(fs);

        mockMvc.perform(get("/firestation").param("address", "1509 Culver St"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.address").value("1509 Culver St"))
                .andExpect(jsonPath("$.station").value("3"));

        verify(fireStationService).get(any(FireStation.class));
    }

    @Test
    void getFireStation_failure_throws() throws Exception {
        when(fireStationService.get(any(FireStation.class)))
                .thenThrow(new RuntimeException("Error encountered"));

        assertThrows(Exception.class, () ->
                mockMvc.perform(get("/firestation").param("address", "1509 Culver St")));

        verify(fireStationService).get(any(FireStation.class));
    }

    @Test
    public void getFireStationListTest() throws Exception {
        FireStation fs1 = new FireStation();
        fs1.setAddress("1509 Culver St");
        fs1.setStation("3");

        FireStation fs2 = new FireStation();
        fs2.setAddress("892 Downing Ct");
        fs2.setStation("2");

        when(fireStationService.findAll()).thenReturn(List.of(fs1, fs2));

        mockMvc.perform(get("/firestations"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].address").value("1509 Culver St"))
                .andExpect(jsonPath("$[1].address").value("892 Downing Ct"))
                .andExpect(jsonPath("$[0].station").value("3"));

        verify(fireStationService).findAll();
    }

    @Test
    void getFireStationList_failure_throws() throws Exception {
        when(fireStationService.findAll())
                .thenThrow(new RuntimeException("Error encountered"));

        assertThrows(Exception.class, () ->
                mockMvc.perform(get("/firestations")));

        verify(fireStationService).findAll();
    }
}
