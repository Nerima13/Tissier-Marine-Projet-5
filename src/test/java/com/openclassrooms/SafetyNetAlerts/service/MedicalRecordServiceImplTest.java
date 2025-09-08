package com.openclassrooms.SafetyNetAlerts.service;

import com.openclassrooms.SafetyNetAlerts.model.MedicalRecord;
import com.openclassrooms.SafetyNetAlerts.repository.FireStationRepository;
import com.openclassrooms.SafetyNetAlerts.repository.MedicalRecordRepository;
import com.openclassrooms.SafetyNetAlerts.repository.PersonRepository;
import com.openclassrooms.SafetyNetAlerts.service.impl.MedicalRecordServiceImpl;
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
public class MedicalRecordServiceImplTest {

    @Mock MedicalRecordRepository medicalRecordRepository;

    @Mock PersonRepository personRepository;
    @Mock FireStationRepository fireStationRepository;
    @Mock IJsonWriter jsonWriter;

    @InjectMocks
    MedicalRecordServiceImpl service;

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
    public void add_failure_throws() {
        MedicalRecord mr = medicalRecord("John","Boyd","03/06/1984",
                List.of("aznol:350mg","hydrapermazol:100mg"), List.of("nillacilan"));
        doThrow(new RuntimeException("Error encountered")).when(medicalRecordRepository).add(any(MedicalRecord.class));

        assertThrows(RuntimeException.class, () -> service.add(mr));

        verify(medicalRecordRepository).add(mr);
    }

    @Test
    public void deleteToRepoTest() {
        MedicalRecord mr = medicalRecord("John","Boyd","03/06/1984", List.of(), List.of());
        service.delete(mr);
        verify(medicalRecordRepository).delete(mr);
    }

    @Test
    public void delete_failure_throws() {
        MedicalRecord mr = medicalRecord("John","Boyd","03/06/1984", List.of(), List.of());
        doThrow(new RuntimeException("Error encountered")).when(medicalRecordRepository).delete(any(MedicalRecord.class));

        assertThrows(RuntimeException.class, () -> service.delete(mr));

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
    public void update_failure_throws() {
        MedicalRecord mr = medicalRecord("John","Boyd","03/06/1984",
                List.of("aznol:350mg"), List.of());
        doThrow(new RuntimeException("Error encountered")).when(medicalRecordRepository).update(any(MedicalRecord.class));

        assertThrows(RuntimeException.class, () -> service.update(mr));

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
    public void getFromRepo_failure_throws() {
        MedicalRecord key = medicalRecord("John","Boyd", null, null, null);

        when(medicalRecordRepository.get(any(MedicalRecord.class))).thenThrow(new RuntimeException("Error encountered"));

        assertThrows(RuntimeException.class, () -> service.get(key));

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

    @Test
    public void findAllFromRepo_failure_throws() {
        when(medicalRecordRepository.findAll()).thenThrow(new RuntimeException("Error encountered"));

        assertThrows(RuntimeException.class, () -> service.findAll());

        verify(medicalRecordRepository).findAll();
    }
}
