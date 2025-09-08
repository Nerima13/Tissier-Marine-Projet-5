package com.openclassrooms.SafetyNetAlerts.service;

import com.openclassrooms.SafetyNetAlerts.model.FireStation;
import com.openclassrooms.SafetyNetAlerts.repository.FireStationRepository;
import com.openclassrooms.SafetyNetAlerts.repository.MedicalRecordRepository;
import com.openclassrooms.SafetyNetAlerts.repository.PersonRepository;
import com.openclassrooms.SafetyNetAlerts.service.impl.FireStationServiceImpl;
import com.openclassrooms.SafetyNetAlerts.writer.IJsonWriter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FireStationServiceImplTest {

    @Mock
    FireStationRepository fireStationRepository;

    @Mock PersonRepository personRepository;
    @Mock MedicalRecordRepository medicalRecordRepository;
    @Mock IJsonWriter jsonWriter;

    @InjectMocks
    FireStationServiceImpl service;

    private FireStation fs(String address, String station) {
        FireStation f = new FireStation();
        f.setAddress(address);
        f.setStation(station);
        return f;
    }

    @Test
    public void addToRepoTest() {
        FireStation f = fs("1509 Culver St", "3");
        service.add(f);
        verify(fireStationRepository).add(f);
    }

    @Test
    public void addToRepo_failure_throws() {
        FireStation f = fs("1509 Culver St", "3");
        doThrow(new RuntimeException("Error encountered")).when(fireStationRepository).add(any(FireStation.class));

        assertThrows(RuntimeException.class, () -> service.add(f));

        verify(fireStationRepository).add(f);
    }

    @Test
    public void deleteToRepoTest() {
        FireStation f = fs("1509 Culver St", "3");
        service.delete(f);
        verify(fireStationRepository).delete(f);
    }

    @Test
    public void deleteToRepo_failure_throws() {
        FireStation f = fs("1509 Culver St", "3");
        doThrow(new RuntimeException("Error encountered")).when(fireStationRepository).delete(any(FireStation.class));

        assertThrows(RuntimeException.class, () -> service.delete(f));

        verify(fireStationRepository).delete(f);
    }

    @Test
    public void updateToRepoTest() {
        FireStation f = fs("1509 Culver St", "3");
        service.update(f);
        verify(fireStationRepository).update(f);
    }

    @Test
    public void updateToRepo_failure_throws() {
        FireStation f = fs("1509 Culver St", "3");
        doThrow(new RuntimeException("Error encountered")).when(fireStationRepository).update(any(FireStation.class));

        assertThrows(RuntimeException.class, () -> service.update(f));

        verify(fireStationRepository).update(f);
    }

    @Test
    public void getFromRepoTest() {
        FireStation key = fs("1509 Culver St", null);
        FireStation stored = fs("1509 Culver St", "3");
        when(fireStationRepository.get(any(FireStation.class))).thenReturn(stored);

        FireStation result = service.get(key);

        assertNotNull(result);
        assertEquals("1509 Culver St", result.getAddress());
        assertEquals("3", result.getStation());
        verify(fireStationRepository).get(key);
    }

    @Test
    public void getFromRepo_failure_throws() {
        FireStation key = fs("1509 Culver St", null);
        when(fireStationRepository.get(any(FireStation.class))).thenThrow(new RuntimeException("Error encountered"));

        assertThrows(RuntimeException.class, () -> service.get(key));

        verify(fireStationRepository).get(key);
    }

    @Test
    public void findAllFromRepoTest() {
        when(fireStationRepository.findAll()).thenReturn(List.of(fs("1509 Culver St", "3")));

        List<FireStation> all = service.findAll();

        assertNotNull(all);
        assertEquals(1, all.size());
        assertEquals("1509 Culver St", all.get(0).getAddress());
        assertEquals("3", all.get(0).getStation());
        verify(fireStationRepository).findAll();
    }

    @Test
    public void findAllFromRepo_failure_throws() {
        when(fireStationRepository.findAll()).thenThrow(new RuntimeException("Error encountered"));

        assertThrows(RuntimeException.class, () -> service.findAll());

        verify(fireStationRepository).findAll();
    }
}