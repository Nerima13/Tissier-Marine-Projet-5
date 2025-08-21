package com.openclassrooms.SafetyNetAlerts.repository;

import com.openclassrooms.SafetyNetAlerts.model.FireStation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class FireStationRepositoryTest {

    @Autowired
    @Qualifier("fireStationRepoSingleton")
    private FireStationRepository fireStationRepository;

    @BeforeEach
    public void setUp() {
        fireStationRepository.clear();
    }

    @Test
    public void addFireStationTest() {
        // Arrange
        FireStation fireStation = new FireStation();
        fireStation.setAddress("address");
        int oldSize = fireStationRepository.findAll().size();

        // Act
        fireStationRepository.add(fireStation);

        // Assert
        List<FireStation> list = fireStationRepository.findAll();
        assertEquals(oldSize + 1, list.size()); // // the size has increased
        assertEquals(fireStation, list.get(list.size() - 1)); // the last element is the one added
    }

    @Test
    public void deleteFireStationTest() {
        // Arrange
        FireStation fireStation = new FireStation();
        fireStation.setAddress("address");
        fireStationRepository.add(fireStation);
        int oldSize = fireStationRepository.findAll().size();

        // Act
        fireStationRepository.delete(fireStation);

        // Assert
        List<FireStation> list = fireStationRepository.findAll();
        assertEquals(oldSize - 1, list.size()); // the size has decreased

        // Vérifie qu'on ne retrouve plus la caserne
        FireStation deletedFireStation = new FireStation();
        deletedFireStation.setAddress("address");
    }

    @Test
    public void updateFireStationTest() {
        // Arrange
        FireStation fireStationAdd = new FireStation();
        fireStationAdd.setAddress("address");
        fireStationRepository.add(fireStationAdd);

        FireStation fireStationUpdate = new FireStation();
        fireStationUpdate.setAddress("address");
        fireStationUpdate.setStation("station");

        // Act
        fireStationRepository.update(fireStationUpdate);

        // Assert
        List<FireStation> list = fireStationRepository.findAll();
        assertTrue(list.contains(fireStationAdd)); // check that the element still exists
        assertEquals("station", fireStationAdd.getStation()); // the station has been updated
    }
}
