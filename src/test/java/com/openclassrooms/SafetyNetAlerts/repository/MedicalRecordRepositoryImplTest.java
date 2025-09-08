package com.openclassrooms.SafetyNetAlerts.repository;

import com.openclassrooms.SafetyNetAlerts.model.MedicalRecord;
import com.openclassrooms.SafetyNetAlerts.repository.impl.MedicalRecordRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class MedicalRecordRepositoryImplTest {

    private MedicalRecordRepositoryImpl repository;

    private MedicalRecord record(String firstName, String lastName, String birthdate, List<String> medications, List<String> allergies) {
        MedicalRecord mr = new MedicalRecord();
        mr.setFirstName(firstName);
        mr.setLastName(lastName);
        mr.setBirthdate(birthdate);
        mr.setMedications(medications);
        mr.setAllergies(allergies);
        return mr;
    }

    @BeforeEach
    public void setUp() {
        repository = new MedicalRecordRepositoryImpl();
    }

    @Test
    public void addMedicalRecordTest() {
        MedicalRecord r = record("John", "Boyd", "03/06/1984", List.of("aznol:350mg", "hydrapermazol:100mg"), List.of("nillacilan"));

        repository.add(r);

        assertEquals(1, repository.findAll().size());
        assertNotNull(repository.get(r));
    }

    @Test
    public void deleteMedicalRecordTest() {
        MedicalRecord r = record("John", "Boyd", "03/06/1984", List.of("aznol:350mg"), List.of("nillacilan"));
        repository.add(r);

        repository.delete(r);

        assertEquals(0, repository.findAll().size());
        assertNull(repository.get(r));
    }

    @Test
    public void updateMedicalRecordTest() {
        MedicalRecord r = record("John", "Boyd", "03/06/1984", List.of("aznol:350mg"), List.of("nillacilan"));
        repository.add(r);

        MedicalRecord modified = record("John", "Boyd", "04/06/1984", List.of("aznol:350mg"), List.of("nillacilan"));

        repository.update(modified);

        MedicalRecord saved = repository.get(r);
        assertNotNull(saved);
        assertEquals("04/06/1984", saved.getBirthdate());
    }

    @Test
    public void findAllMedicalRecordsTest() {
        MedicalRecord r1 = record("John", "Boyd", "03/06/1984", List.of("aznol:350mg"), List.of("nillacilan"));
        MedicalRecord r2 = record("Peter", "Duncan", "09/06/2007", List.of("pharmacol:5000mg"), List.of("shellfish"));

        repository.add(r1);
        repository.add(r2);

        assertEquals(2, repository.findAll().size());
        assertNotNull(repository.get(r1));
        assertNotNull(repository.get(r2));
    }

    @Test
    public void clearMedicalRecordsTest() {
        MedicalRecord r1 = record("John", "Boyd", "03/06/1984", List.of("aznol:350mg"), List.of("nillacilan"));
        MedicalRecord r2 = record("Peter", "Duncan", "09/06/2007", List.of("pharmacol:5000mg"), List.of("shellfish"));

        repository.add(r1);
        repository.add(r2);

        repository.clear();

        assertTrue(repository.findAll().isEmpty());
        assertNull(repository.get(r1));
        assertNull(repository.get(r2));
    }
}
