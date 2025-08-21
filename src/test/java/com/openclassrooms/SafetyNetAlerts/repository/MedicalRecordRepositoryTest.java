package com.openclassrooms.SafetyNetAlerts.repository;

import com.openclassrooms.SafetyNetAlerts.model.MedicalRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class MedicalRecordRepositoryTest {

    @Autowired
    @Qualifier("medicalRecordRepoSingleton")
    private MedicalRecordRepository medicalRecordRepository;

    @BeforeEach
    public void setUp() {
        medicalRecordRepository.clear();
    }

    @Test
    public void addMedicalRecordTest() {
        // Arrange
        MedicalRecord medicalRecord = new MedicalRecord();
        medicalRecord.setFirstName("firstName");
        medicalRecord.setLastName("lastName");
        int oldSize = medicalRecordRepository.findAll().size();

        // Act
        medicalRecordRepository.add(medicalRecord);

        // Assert
        List<MedicalRecord> list = medicalRecordRepository.findAll();
        assertEquals(oldSize + 1, list.size()); // the size has increased
        assertEquals(medicalRecord, list.get(list.size() - 1)); // the last element is the one added
    }

    @Test
    public void deleteMedicalRecordTest() {
        // Arrange
        MedicalRecord medicalRecord = new MedicalRecord();
        medicalRecord.setFirstName("firstName");
        medicalRecord.setLastName("lastName");
        medicalRecordRepository.add(medicalRecord);
        int oldSize = medicalRecordRepository.findAll().size();

        // Act
        medicalRecordRepository.delete(medicalRecord);

        // Assert
        List<MedicalRecord> list = medicalRecordRepository.findAll();
        assertEquals(oldSize - 1, list.size()); // the size has decreased

        // Check that the medical record is no longer found
        MedicalRecord deletedMedicalRecord = new MedicalRecord();
        deletedMedicalRecord.setFirstName("firstName");
        deletedMedicalRecord.setLastName("lastName");
        assertFalse(list.contains(deletedMedicalRecord));
    }

    @Test
    public void updateMedicalRecordTest() {
        // Arrange
        MedicalRecord medicalRecordAdd = new MedicalRecord();
        medicalRecordAdd.setFirstName("firstName");
        medicalRecordAdd.setLastName("lastName");
        medicalRecordRepository.add(medicalRecordAdd);

        MedicalRecord medicalRecordUpdate = new MedicalRecord();
        medicalRecordUpdate.setFirstName("firstName");
        medicalRecordUpdate.setLastName("lastName");
        medicalRecordUpdate.setBirthdate("birthdate");

        // Act
        medicalRecordRepository.update(medicalRecordUpdate);

        // Assert
        List<MedicalRecord> list = medicalRecordRepository.findAll();
        assertTrue(list.contains(medicalRecordAdd)); // check that the element still exists
        assertEquals("birthdate", medicalRecordAdd.getBirthdate()); // the birthdate has been updated
    }
}

