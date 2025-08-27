package com.openclassrooms.SafetyNetAlerts.service;

import com.openclassrooms.SafetyNetAlerts.model.FireStation;
import com.openclassrooms.SafetyNetAlerts.repository.FireStationRepository;
import com.openclassrooms.SafetyNetAlerts.service.impl.FireStationServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FireStationServiceImplTest {

    @Mock
    FireStationRepository fireStationRepository;

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
    public void deleteToRepoTest() {
        FireStation f = fs("1509 Culver St", "3");
        service.delete(f);
        verify(fireStationRepository).delete(f);
    }

    @Test
    public void updateToRepoTest() {
        FireStation f = fs("1509 Culver St", "3");
        service.update(f);
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
    public void findAllFromRepoTest() {
        when(fireStationRepository.findAll()).thenReturn(List.of(fs("1509 Culver St", "3")));

        List<FireStation> all = service.findAll();

        assertNotNull(all);
        assertEquals(1, all.size());
        assertEquals("1509 Culver St", all.get(0).getAddress());
        assertEquals("3", all.get(0).getStation());
        verify(fireStationRepository).findAll();
    }
}