package com.openclassrooms.SafetyNetAlerts.service;

import com.openclassrooms.SafetyNetAlerts.dto.request.childAlert.ChildDTO;
import com.openclassrooms.SafetyNetAlerts.dto.request.fireAlert.FireAlertAddressDTO;
import com.openclassrooms.SafetyNetAlerts.dto.request.fireAlert.FireAlertPersonDTO;
import com.openclassrooms.SafetyNetAlerts.dto.request.firestationNumber.FireStationCoverageDTO;
import com.openclassrooms.SafetyNetAlerts.dto.request.firestationNumber.PersonByStationNumberDTO;
import com.openclassrooms.SafetyNetAlerts.dto.request.floodAlert.FloodAlertAddressDTO;
import com.openclassrooms.SafetyNetAlerts.dto.request.floodAlert.FloodAlertDTO;
import com.openclassrooms.SafetyNetAlerts.dto.request.floodAlert.FloodAlertPersonDTO;
import com.openclassrooms.SafetyNetAlerts.dto.request.personInfo.PersonInfoDTO;
import com.openclassrooms.SafetyNetAlerts.model.FireStation;
import com.openclassrooms.SafetyNetAlerts.model.MedicalRecord;
import com.openclassrooms.SafetyNetAlerts.model.Person;
import com.openclassrooms.SafetyNetAlerts.repository.PersonRepository;
import com.openclassrooms.SafetyNetAlerts.service.impl.PersonServiceImpl;
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
public class PersonServiceImplTest {

    @Mock FireStationService fireStationService;
    @Mock MedicalRecordService medicalRecordService;
    @Mock PersonRepository personRepository;

    @InjectMocks
    PersonServiceImpl service;

    private Person person(String firstName, String lastName, String address, String city, String zip, String phone, String email) {
        Person p = new Person();
        p.setFirstName(firstName);
        p.setLastName(lastName);
        p.setAddress(address);
        p.setCity(city);
        p.setPhone(phone);
        p.setEmail(email);
        return p;
    }
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
        Person p = person("John","Boyd","1509 Culver St","Culver","97451","841-874-6512", "jaboyd@email.com");
        service.add(p);
        verify(personRepository).add(p);
    }

    @Test
    void addToRepo_failure_throws() {
        Person p = person("John","Boyd","1509 Culver St","Culver","97451","841-874-6512","jaboyd@email.com");
        doThrow(new RuntimeException("Error encountered")).when(personRepository).add(any(Person.class));

        assertThrows(RuntimeException.class, () -> service.add(p));

        verify(personRepository).add(p);
    }

    @Test
    public void deleteToRepoTest() {
        Person p = person("John","Boyd","1509 Culver St","Culver","97451","841-874-6512", "jaboyd@email.com");
        service.delete(p);
        verify(personRepository).delete(p);
    }

    @Test
    void deleteToRepo_failure_throws() {
        Person p = person("John","Boyd","1509 Culver St","Culver","97451","841-874-6512","jaboyd@email.com");
        doThrow(new RuntimeException("Error encountered")).when(personRepository).delete(any(Person.class));

        assertThrows(RuntimeException.class, () -> service.delete(p));

        verify(personRepository).delete(p);
    }

    @Test
    public void updateToRepoTest() {
        Person p = person("John","Boyd","1509 Culver St","Culver","97451","841-874-6512", "jaboyd@email.com");
        service.update(p);
        verify(personRepository).update(p);
    }

    @Test
    void updateToRepo_failure_throws() {
        Person p = person("John","Boyd","1509 Culver St","Culver","97451","841-874-6512","jaboyd@email.com");
        doThrow(new RuntimeException("Error encountered")).when(personRepository).update(any(Person.class));

        assertThrows(RuntimeException.class, () -> service.update(p));

        verify(personRepository).update(p);
    }

    @Test
    public void getFromRepoTest() {
        Person key = person("John", "Boyd", null, null, null, null, null);
        Person stored = person("John","Boyd","1509 Culver St","Culver","97451","841-874-6512", "jaboyd@email.com");
        when(personRepository.get(any(Person.class))).thenReturn(stored);

        Person result = service.get(key);

        assertNotNull(result);
        assertEquals("John", result.getFirstName());
        assertEquals("Boyd", result.getLastName());
        verify(personRepository).get(key);
    }

    @Test
    void getFromRepo_failure_throws() {
        Person key = person("John","Boyd",null,null,null,null,null);
        when(personRepository.get(any(Person.class))).thenThrow(new RuntimeException("Error encountered"));

        assertThrows(RuntimeException.class, () -> service.get(key));

        verify(personRepository).get(key);
    }

    @Test
    public void findAllFromRepo() {
        when(personRepository.findAll()).thenReturn(List.of(person("firstName","lastName","addr","city","zip","phone", "mail")));
        assertEquals(1, service.findAll().size());
        verify(personRepository).findAll();
    }

    @Test
    void findAllFromRepo_failure_throws() {
        when(personRepository.findAll()).thenThrow(new RuntimeException("Error encountered"));

        assertThrows(RuntimeException.class, () -> service.findAll());

        verify(personRepository).findAll();
    }

    @Test
    public void getAgeTest() {
        Person p = person("John","Boyd",null,null,null,null, null);
        when(medicalRecordService.get(any(MedicalRecord.class))).thenReturn(medicalRecord("John","Boyd","03/06/1984", List.of(), List.of()));

        int age = service.getAge(p);

        assertTrue(age >= 20);
    }

    @Test
    void getAge_failure_whenMedicalRecordGetThrows() {
        Person p = person("John","Boyd",null,null,null,null,null);
        when(medicalRecordService.get(any(MedicalRecord.class))).thenThrow(new IllegalStateException("Error encountered"));

        assertThrows(IllegalStateException.class, () -> service.getAge(p));

        verify(medicalRecordService).get(any(MedicalRecord.class));
    }

    @Test
    public void getPersonsCoveredByStationTest() {
        when(fireStationService.findAll()).thenReturn(List.of(
                new FireStation("1509 Culver St", "3"),
                new FireStation("644 Gershwin Cir", "1")));


        Person p1 = person("John","Boyd","1509 Culver St","Culver","97451","841-874-6512", "jaboyd@email.com");
        Person p2 = person("Peter", "Duncan", "644 Gershwin Cir", "Culver", "97451", "841-874-6512", "jaboyd@email.com");
        when(personRepository.findAll()).thenReturn(List.of(p1, p2));

        when(medicalRecordService.get(any(MedicalRecord.class))).thenReturn(
                medicalRecord("John", "Boyd", "03/06/1984", List.of(), List.of()));

        FireStationCoverageDTO dto = service.getPersonsCoveredByStation("3");

        assertNotNull(dto);
        assertEquals("3", dto.getStation());
        assertEquals(1, dto.getInfoPerson().size());

        PersonByStationNumberDTO first = dto.getInfoPerson().get(0);
        assertEquals("John", first.getFirstName());
        assertEquals("Boyd", first.getLastName());
        assertEquals("1509 Culver St", first.getAddress());

        assertEquals(1, dto.getNbAdult());
        assertEquals(0, dto.getNbChild());

        verify(fireStationService).findAll();
        verify(personRepository).findAll();
        verify(medicalRecordService, times(1)).get(any(MedicalRecord.class));
    }

    @Test
    public void getChildInfosTest() {
        Person p1 = person("John", "Boyd", "1509 Culver St", "Culver", "97451", "841-874-6512", "jaboyd@email.com");
        Person p2 = person("Tenley", "Boyd", "1509 Culver St", "Culver", "97451", "841-874-6512", "tenz@email.com");
        Person p3 = person("Tessa", "Carman", "834 Binoc Ave", "Culver", "97451", "841-874-6512", "tenz@email.com");

        when(personRepository.findAll()).thenReturn(List.of(p1, p2, p3));

        when(medicalRecordService.get(any(MedicalRecord.class))).thenReturn(
                medicalRecord("John","Boyd","03/06/1984", List.of(), List.of()),
                medicalRecord("Tenley","Boyd","02/18/2012", List.of(), List.of()));

        List<ChildDTO> result = service.getChildInfos("1509 Culver St");

        assertNotNull(result);
        assertEquals(1, result.size());

        ChildDTO child1 = result.get(0);
        assertEquals("Tenley", child1.getFirstName());
        assertEquals("Boyd", child1.getLastName());
        assertTrue(child1.getAge() <= 18);
        assertNotNull(child1.getFamily());
        assertEquals(1, child1.getFamily().size());
        assertEquals("John", child1.getFamily().get(0).getFirstName());

        verify(personRepository).findAll();
        verify(medicalRecordService, times(2)).get(any(MedicalRecord.class));
    }

    @Test
    public void getPhoneByFireStationTest() {
        when(fireStationService.findAll()).thenReturn(List.of(
                new FireStation("1509 Culver St", "3"),
                new FireStation("29 15th St", "2")));

        Person p1 = person("John",   "Boyd",   "1509 Culver St", "Culver", "97451", "841-874-6512", "jaboyd@email.com");
        Person p2 = person("Peter",  "Duncan", "644 Gershwin Cir","Culver","97451", "841-874-6512", "jaboyd@email.com");
        when(personRepository.findAll()).thenReturn(List.of(p1, p2));

        List<String> phones = service.getPhoneByFireStation("3");

        assertNotNull(phones);
        assertEquals(1, phones.size());
        assertEquals("841-874-6512", phones.get(0));

        verify(fireStationService).findAll();
        verify(personRepository).findAll();
    }

    @Test
    public void getFireAlertTest() {
        when(fireStationService.findAll()).thenReturn(List.of(
                new FireStation("1509 Culver St", "3"),
                new FireStation("834 Binoc Ave", "3")));

        Person p1 = person("John", "Boyd", "1509 Culver St", "Culver", "97451", "841-874-6512", "jaboyd@email.com");
        Person p2 = person("Peter", "Duncan", "644 Gershwin Cir", "Culver", "97451", "841-874-6512", "jaboyd@email.com"); // non couvert
        when(personRepository.findAll()).thenReturn(List.of(p1, p2));

        when(medicalRecordService.get(any(MedicalRecord.class))).thenReturn(medicalRecord("John", "Boyd", "03/06/1984",
                        List.of("aznol:350mg", "hydrapermazol:100mg"),
                        List.of("nillacilan")));

        FireAlertAddressDTO dto = service.getFireAlert("1509 Culver St");

        assertNotNull(dto);
        assertEquals("3", dto.getStation());
        assertNotNull(dto.getResidents());
        assertEquals(1, dto.getResidents().size());

        FireAlertPersonDTO r = dto.getResidents().get(0);
        assertEquals("John", r.getFirstName());
        assertEquals("Boyd", r.getLastName());
        assertEquals("841-874-6512", r.getPhone());
        assertEquals(List.of("aznol:350mg", "hydrapermazol:100mg"), r.getMedications());
        assertEquals(List.of("nillacilan"), r.getAllergies());
        assertTrue(r.getAge() > 0);

        verify(fireStationService).findAll();
        verify(personRepository).findAll();
        verify(medicalRecordService, times(1)).get(any(MedicalRecord.class));
    }

    @Test
    public void getFloodAlertTest() {
        when(fireStationService.findAll()).thenReturn(List.of(
                new FireStation("29 15th St", "2")));

        Person p1 = person("Jonanathan", "Marrack", "29 15th St", "Culver", "97451", "841-874-6513", "drk@email.com");
        Person p2 = person("Peter", "Duncan", "644 Gershwin Cir", "Culver", "97451", "841-874-6512", "jaboyd@email.com");
        when(personRepository.findAll()).thenReturn(List.of(p1, p2));

        when(medicalRecordService.get(any(MedicalRecord.class))).thenReturn(
                medicalRecord("Jonanathan", "Marrack", "01/03/1989", List.of(), List.of()));

        FloodAlertDTO dto = service.getFloodAlert(List.of("2"));

        assertNotNull(dto);
        assertEquals(List.of("2"), dto.getStations());
        assertNotNull(dto.getAddressList());
        assertEquals(1, dto.getAddressList().size());
        assertEquals("29 15th St", dto.getAddressList().get(0).getAddress());

        FloodAlertAddressDTO a = dto.getAddressList().get(0);
        assertEquals(1, a.getListPerson().size());
        FloodAlertPersonDTO p = a.getListPerson().get(0);
        assertEquals("Jonanathan", p.getFirstName());
        assertEquals("Marrack", p.getLastName());
        assertEquals("841-874-6513", p.getPhone());
        assertTrue(p.getMedications().isEmpty());
        assertTrue(p.getAllergies().isEmpty());
        assertTrue(p.getAge() >= 0);

        verify(fireStationService).findAll();
        verify(personRepository).findAll();
        verify(medicalRecordService, times(1)).get(any(MedicalRecord.class));
    }

    @Test
    public void getPersonInfoTest() {
        Person p1 = person("Foster","Shepard","748 Townings Dr","Culver","97451","841-874-6544","jaboyd@email.com");
        Person p2 = person("Sophia","Zemicks","892 Downing Ct","Culver","97451","841-874-7878","soph@email.com");
        when(personRepository.findAll()).thenReturn(List.of(p1, p2));

        MedicalRecord mrFoster = medicalRecord("Foster","Shepard","01/08/1980", List.of(), List.of());
        when(medicalRecordService.findAll()).thenReturn(List.of(mrFoster));
        when(medicalRecordService.get(any(MedicalRecord.class))).thenReturn(mrFoster);

        List<PersonInfoDTO> infos = service.getPersonInfo("Shepard");

        assertNotNull(infos);
        assertEquals(1, infos.size());

        PersonInfoDTO p = infos.get(0);
        assertEquals("Foster", p.getFirstName());
        assertEquals("Shepard", p.getLastName());
        assertEquals("748 Townings Dr", p.getAddress());
        assertEquals("jaboyd@email.com", p.getEmail());
        assertTrue(p.getMedications().isEmpty());
        assertTrue(p.getAllergies().isEmpty());
        assertTrue(p.getAge() >= 0);

        verify(personRepository).findAll();
        verify(medicalRecordService).findAll();
        verify(medicalRecordService, times(1)).get(any(MedicalRecord.class));
    }

    @Test
    public void getEmailTest() {
        Person p1  = person("John","Boyd","1509 Culver St","Culver","97451","841-874-6512","jaboyd@email.com");
        Person p2  = person("Sophia","Zemicks","892 Downing Ct","Culver","97451","841-874-7878","soph@email.com");
        when(personRepository.findAll()).thenReturn(List.of(p1, p2));

        List<String> emails = service.getEmail("Culver");

        assertNotNull(emails);
        assertEquals(2, emails.size());
        assertEquals("jaboyd@email.com", emails.get(0));
        assertEquals("soph@email.com", emails.get(1));

        verify(personRepository).findAll();
    }
}
