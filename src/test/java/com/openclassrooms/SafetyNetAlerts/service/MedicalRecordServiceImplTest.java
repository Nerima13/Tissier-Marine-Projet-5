package com.openclassrooms.SafetyNetAlerts.service;

import com.openclassrooms.SafetyNetAlerts.model.MedicalRecord;
import com.openclassrooms.SafetyNetAlerts.repository.MedicalRecordRepository;
import com.openclassrooms.SafetyNetAlerts.service.impl.MedicalRecordServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MedicalRecordServiceImplTest {

    @Mock
    MedicalRecordRepository medicalRecordRepository;

    @InjectMocks
    MedicalRecordServiceImpl service;

    // Helper pour créer rapidement un MR
    private MedicalRecord medicalRecord(String firstName, String lastName, String birthdate, List<String> medications, List<String> allergies) {
        MedicalRecord mr = new MedicalRecord();
        mr.setFirstName(firstName);
        mr.setLastName(lastName);
        mr.setBirthdate(birthdate);
        mr.setMedications(medications);
        mr.setAllergies(allergies);
        return mr;
    }

    @Test
    public void addToRepoTest() {
        MedicalRecord mr = medicalRecord("John","Boyd","03/06/1984",
                List.of("aznol:350mg","hydrapermazol:100mg"), List.of("nillacilan"));
        service.add(mr);
        verify(medicalRecordRepository).add(mr);
    }

    @Test
    public void deleteToRepoTest() {
        MedicalRecord mr = medicalRecord("John","Boyd","03/06/1984", List.of(), List.of());
        service.delete(mr);
        verify(medicalRecordRepository).delete(mr);
    }

    @Test
    public void updateToRepoTest() {
        MedicalRecord mr = medicalRecord("John","Boyd","03/06/1984",
                List.of("aznol:350mg"), List.of());
        service.update(mr);
        verify(medicalRecordRepository).update(mr);
    }

    @Test
    public void getFromRepoTest() {
        MedicalRecord key = medicalRecord("John","Boyd", null, null, null);
        MedicalRecord stored = medicalRecord("John","Boyd","03/06/1984",
                List.of("aznol:350mg","hydrapermazol:100mg"), List.of("nillacilan"));

        when(medicalRecordRepository.get(any(MedicalRecord.class))).thenReturn(stored);

        MedicalRecord result = service.get(key);

        assertNotNull(result);
        assertEquals("John", result.getFirstName());
        assertEquals("Boyd", result.getLastName());
        assertEquals("03/06/1984", result.getBirthdate());
        assertEquals(List.of("aznol:350mg","hydrapermazol:100mg"), result.getMedications());
        assertEquals(List.of("nillacilan"), result.getAllergies());

        verify(medicalRecordRepository).get(key);
    }

    @Test
    public void findAllFromRepoTest() {
        when(medicalRecordRepository.findAll()).thenReturn(
                List.of(medicalRecord("John","Boyd","03/06/1984", List.of(), List.of())));

        List<MedicalRecord> all = service.findAll();

        assertNotNull(all);
        assertEquals(1, all.size());
        verify(medicalRecordRepository).findAll();
    }
}
