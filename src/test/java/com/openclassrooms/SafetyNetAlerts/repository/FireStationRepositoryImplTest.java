package com.openclassrooms.SafetyNetAlerts.repository;

import com.openclassrooms.SafetyNetAlerts.model.FireStation;
import com.openclassrooms.SafetyNetAlerts.repository.impl.FireStationRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class FireStationRepositoryImplTest {

    private FireStationRepositoryImpl repository;

    private FireStation fs(String address, String station) {
        FireStation f = new FireStation();
        f.setAddress(address);
        f.setStation(station);
        return f;
    }

    @BeforeEach
    public void setUp() {
        repository = new FireStationRepositoryImpl();
    }

    @Test
    public void addFireStationTest() {
        FireStation f = fs("1509 Culver St", "3");

        repository.add(f);

        assertEquals(1, repository.findAll().size());
        assertNotNull(repository.get(f));
    }

    @Test
    public void deleteFireStationTest() {
        FireStation f = fs("1509 Culver St", "3");
        repository.add(f);

        repository.delete(f);

        assertEquals(0, repository.findAll().size());
        assertNull(repository.get(f));
    }

    @Test
    public void updateFireStationTest() {
        FireStation original = fs("1509 Culver St", "3");
        repository.add(original);

        FireStation modified = fs("1509 Culver St", "4");
        repository.update(modified);

        FireStation saved = repository.get(modified);
        assertNotNull(saved);
        assertEquals("4", saved.getStation());
    }

    @Test
    public void findAllFireStationsTest() {
        FireStation f1 = fs("1509 Culver St", "3");
        FireStation f2 = fs("29 15th St", "2");

        repository.add(f1);
        repository.add(f2);

        assertEquals(2, repository.findAll().size());
        assertNotNull(repository.get(f1));
        assertNotNull(repository.get(f2));
    }

    @Test
    public void clearFireStationsTest() {
        FireStation f1 = fs("1509 Culver St", "3");
        FireStation f2 = fs("29 15th St", "2");
        repository.add(f1);
        repository.add(f2);

        repository.clear();

        assertTrue(repository.findAll().isEmpty());
        assertNull(repository.get(f1));
        assertNull(repository.get(f2));
    }
}
