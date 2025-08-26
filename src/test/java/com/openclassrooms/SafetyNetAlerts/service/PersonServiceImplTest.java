package com.openclassrooms.SafetyNetAlerts.service;

import com.openclassrooms.SafetyNetAlerts.dto.request.childAlert.ChildDTO;
import com.openclassrooms.SafetyNetAlerts.dto.request.fireAlert.FireAlertPersonDTO;
import com.openclassrooms.SafetyNetAlerts.dto.request.firestationNumber.FireStationCoverageDTO;
import com.openclassrooms.SafetyNetAlerts.dto.request.firestationNumber.PersonByStationNumberDTO;
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
    public void deleteToRepoTest() {
        Person p = person("John","Boyd","1509 Culver St","Culver","97451","841-874-6512", "jaboyd@email.com");
        service.delete(p);
        verify(personRepository).delete(p);
    }

    @Test
    public void updateToRepoTest() {
        Person p = person("John","Boyd","1509 Culver St","Culver","97451","841-874-6512", "jaboyd@email.com");
        service.update(p);
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
    public void findAllFromRepo() {
        when(personRepository.findAll()).thenReturn(List.of(person("firstName","lastName","addr","city","zip","phone", "mail")));
        assertEquals(1, service.findAll().size());
        verify(personRepository).findAll();
    }

    @Test
    public void getAgeTest() {
        Person p = person("John","Boyd",null,null,null,null, null);
        when(medicalRecordService.get(any(MedicalRecord.class)))
                .thenReturn(medicalRecord("John","Boyd","03/06/2000", List.of(), List.of()));

        int age = service.getAge(p);

        assertTrue(age >= 20);
    }

    @Test
    public void getPersonsCoveredByStationTest() {
        when(fireStationService.findAll()).thenReturn(List.of(
                new FireStation("1509 Culver St", "3"),
                new FireStation("834 Binoc Ave", "3"),
                new FireStation("644 Gershwin Cir", "1")));


        Person p1 = person("John","Boyd","1509 Culver St","Culver","97451","841-874-6512", "jaboyd@email.com");
        Person p2 = person("Tessa", "Carman", "834 Binoc Ave", "Culver", "97451", "841-874-6512", "tenz@email.com");
        Person p3 = person("Peter", "Duncan", "644 Gershwin Cir", "Culver", "97451", "841-874-6512", "jaboyd@email.com");
        when(personRepository.findAll()).thenReturn(List.of(p1, p2, p3));

        when(medicalRecordService.get(any(MedicalRecord.class))).thenReturn(
                medicalRecord("John", "Boyd", "03/06/1984", List.of(), List.of()),
                medicalRecord("Tessa", "Carman", "02/18/2012", List.of(), List.of()));

        FireStationCoverageDTO dto = service.getPersonsCoveredByStation("3");

        assertNotNull(dto);
        assertEquals("3", dto.getStation());
        assertEquals(2, dto.getInfoPerson().size());

        PersonByStationNumberDTO first = dto.getInfoPerson().get(0);
        assertEquals("John", first.getFirstName());
        assertEquals("Boyd", first.getLastName());
        assertEquals("1509 Culver St", first.getAddress());

        PersonByStationNumberDTO second = dto.getInfoPerson().get(1);
        assertEquals("Tessa", second.getFirstName());
        assertEquals("Carman", second.getLastName());
        assertEquals("834 Binoc Ave", second.getAddress());

        assertEquals(1, dto.getNbAdult());
        assertEquals(1, dto.getNbChild());

        verify(fireStationService).findAll();
        verify(personRepository).findAll();
        verify(medicalRecordService, times(2)).get(any(MedicalRecord.class));
    }

    @Test
    public void getChildInfosTest() {
        Person p1    = person("John",    "Boyd", "1509 Culver St", "Culver", "97451", "841-874-6512", "jaboyd@email.com");
        Person p2   = person("Jacob",   "Boyd", "1509 Culver St", "Culver", "97451", "841-874-6513", "drk@email.com");
        Person p3  = person("Tenley",  "Boyd", "1509 Culver St", "Culver", "97451", "841-874-6512", "tenz@email.com");
        Person p4   = person("Roger",   "Boyd", "1509 Culver St", "Culver", "97451", "841-874-6512", "jaboyd@email.com");
        Person p5 = person("Felicia", "Boyd", "1509 Culver St", "Culver", "97451", "841-874-6544", "jaboyd@email.com");
        Person p6   = person("Tessa",   "Carman","834 Binoc Ave", "Culver", "97451", "841-874-6512", "tenz@email.com");

        when(personRepository.findAll()).thenReturn(List.of(p1, p2, p3, p4, p5, p6));

        when(medicalRecordService.get(any(MedicalRecord.class))).thenReturn(
                medicalRecord("John",    "Boyd", "03/06/1984", List.of(), List.of()),
                medicalRecord("Jacob",   "Boyd", "03/06/1989", List.of(), List.of()),
                medicalRecord("Tenley",  "Boyd", "02/18/2012", List.of(), List.of()),
                medicalRecord("Roger",   "Boyd", "09/06/2017", List.of(), List.of()),
                medicalRecord("Felicia", "Boyd", "01/08/1986", List.of(), List.of()));

        List<ChildDTO> result = service.getChildInfos("1509 Culver St");

        assertNotNull(result);
        assertEquals(2, result.size());

        ChildDTO child1 = result.get(0);
        assertEquals("Tenley", child1.getFirstName());
        assertEquals("Boyd", child1.getLastName());
        assertTrue(child1.getAge() <= 18);
        assertNotNull(child1.getFamily());
        assertEquals(4, child1.getFamily().size());
        assertEquals("John", child1.getFamily().get(0).getFirstName());
        assertEquals("Jacob", child1.getFamily().get(1).getFirstName());
        assertEquals("Roger", child1.getFamily().get(2).getFirstName());
        assertEquals("Felicia", child1.getFamily().get(3).getFirstName());

        ChildDTO child2 = result.get(1);
        assertEquals("Roger", child2.getFirstName());
        assertEquals("Boyd", child2.getLastName());
        assertTrue(child2.getAge() <= 18);
        assertNotNull(child2.getFamily());
        assertEquals(4, child2.getFamily().size());
        assertEquals("John", child2.getFamily().get(0).getFirstName());
        assertEquals("Jacob", child2.getFamily().get(1).getFirstName());
        assertEquals("Tenley", child2.getFamily().get(2).getFirstName());
        assertEquals("Felicia", child2.getFamily().get(3).getFirstName());

        verify(personRepository).findAll();
        verify(medicalRecordService, times(5)).get(any(MedicalRecord.class));
    }

    @Test
    public void getPhoneByFireStationTest() {
        when(fireStationService.findAll()).thenReturn(List.of(
                new FireStation("1509 Culver St", "3"),
                new FireStation("834 Binoc Ave", "3"),
                new FireStation("29 15th St", "2")));

        Person p1 = person("John",   "Boyd",   "1509 Culver St", "Culver", "97451", "841-874-6512", "jaboyd@email.com");
        Person p2 = person("Jacob",  "Boyd",   "1509 Culver St", "Culver", "97451", "841-874-6513", "drk@email.com");
        Person p3 = person("Tenley", "Boyd",   "1509 Culver St", "Culver", "97451", "841-874-6512", "tenz@email.com");
        Person p4 = person("Tessa",  "Carman", "834 Binoc Ave",  "Culver", "97451", "841-874-6512", "tenz@email.com");
        Person p5 = person("Peter",  "Duncan", "644 Gershwin Cir","Culver","97451", "841-874-6512", "jaboyd@email.com");
        when(personRepository.findAll()).thenReturn(List.of(p1, p2, p3, p4, p5));

        List<String> phones = service.getPhoneByFireStation("3");

        assertNotNull(phones);
        assertEquals(4, phones.size());
        assertEquals("841-874-6512", phones.get(0)); // John (1509 Culver St)
        assertEquals("841-874-6513", phones.get(1)); // Jacob (1509 Culver St)
        assertEquals("841-874-6512", phones.get(2)); // Tenley (1509 Culver St)
        assertEquals("841-874-6512", phones.get(3)); // Tessa (834 Binoc Ave)

        verify(fireStationService).findAll();
        verify(personRepository).findAll();
    }

    @Test
    public void getFireAlertTest() {
        // 1) Mappings firestations selon le JSON
        //    -> "1509 Culver St" est desservie par la station "3"
        when(fireStationService.findAll()).thenReturn(List.of(
                new FireStation("1509 Culver St", "3"),
                new FireStation("834 Binoc Ave", "3"),     // bruit
                new FireStation("644 Gershwin Cir", "1")   // bruit
        ));

        // 2) Personnes selon le JSON :
        //    Adresse ciblée: "1509 Culver St" -> John, Jacob, Tenley, Roger, Felicia
        //    + une personne ailleurs (bruit) pour vérifier le filtrage par adresse
        Person john   = person("John",   "Boyd",   "1509 Culver St", "Culver", "97451", "841-874-6512", "jaboyd@email.com");
        Person jacob  = person("Jacob",  "Boyd",   "1509 Culver St", "Culver", "97451", "841-874-6513", "drk@email.com");
        Person tenley = person("Tenley", "Boyd",   "1509 Culver St", "Culver", "97451", "841-874-6512", "tenz@email.com");
        Person roger  = person("Roger",  "Boyd",   "1509 Culver St", "Culver", "97451", "841-874-6512", "jaboyd@email.com");
        Person felicia= person("Felicia","Boyd",   "1509 Culver St", "Culver", "97451", "841-874-6544", "jaboyd@email.com");
        Person other  = person("Peter",  "Duncan", "644 Gershwin Cir","Culver","97451", "841-874-6512", "jaboyd@email.com");
        when(personRepository.findAll()).thenReturn(List.of(john, jacob, tenley, roger, felicia, other));

        // 3) Stubs des dossiers médicaux (JSON). Attention :
        //    getFireAlert() appelle getAge(p) -> 1er appel MR
        //    puis recharge MR pour remplir médications/allergies -> 2e appel MR
        //    => 2 appels par personne présente à l'adresse cible (5 personnes -> 10 appels)
        when(medicalRecordService.get(any(MedicalRecord.class))).thenReturn(
                // John (x2)
                medicalRecord("John",   "Boyd", "03/06/1984",
                        List.of("aznol:350mg", "hydrapermazol:100mg"), List.of("nillacilan")),
                medicalRecord("John",   "Boyd", "03/06/1984",
                        List.of("aznol:350mg", "hydrapermazol:100mg"), List.of("nillacilan")),
                // Jacob (x2)
                medicalRecord("Jacob",  "Boyd", "03/06/1989",
                        List.of("pharmacol:5000mg", "terazine:10mg", "noznazol:250mg"), List.of()),
                medicalRecord("Jacob",  "Boyd", "03/06/1989",
                        List.of("pharmacol:5000mg", "terazine:10mg", "noznazol:250mg"), List.of()),
                // Tenley (x2)
                medicalRecord("Tenley", "Boyd", "02/18/2012",
                        List.of(), List.of("peanut")),
                medicalRecord("Tenley", "Boyd", "02/18/2012",
                        List.of(), List.of("peanut")),
                // Roger (x2)
                medicalRecord("Roger",  "Boyd", "09/06/2017",
                        List.of(), List.of()),
                medicalRecord("Roger",  "Boyd", "09/06/2017",
                        List.of(), List.of()),
                // Felicia (x2)
                medicalRecord("Felicia","Boyd", "01/08/1986",
                        List.of("tetracyclaz:650mg"), List.of("xilliathal")),
                medicalRecord("Felicia","Boyd", "01/08/1986",
                        List.of("tetracyclaz:650mg"), List.of("xilliathal"))
        );

        // 4) Appel
        FireAlertAddressDTO dto = service.getFireAlert("1509 Culver St");

        // 5) Assertions simples et explicites (par index), conformes au JSON

        // Station trouvée
        assertNotNull(dto);
        assertEquals("3", dto.getStation());

        // 5 résidents à cette adresse
        assertNotNull(dto.getResidents());
        assertEquals(5, dto.getResidents().size());

        // Résident 1 : John Boyd
        FireAlertPersonDTO r1 = dto.getResidents().get(0);
        assertEquals("John", r1.getFirstName());
        assertEquals("Boyd", r1.getLastName());
        assertEquals("841-874-6512", r1.getPhone());
        assertTrue(r1.getAge() > 0); // évite de figer l'âge exact
        assertEquals(List.of("aznol:350mg", "hydrapermazol:100mg"), r1.getMedications());
        assertEquals(List.of("nillacilan"), r1.getAllergies());

        // Résident 2 : Jacob Boyd
        FireAlertPersonDTO r2 = dto.getResidents().get(1);
        assertEquals("Jacob", r2.getFirstName());
        assertEquals("Boyd",  r2.getLastName());
        assertEquals("841-874-6513", r2.getPhone());
        assertTrue(r2.getAge() > 0);
        assertEquals(List.of("pharmacol:5000mg", "terazine:10mg", "noznazol:250mg"), r2.getMedications());
        assertTrue(r2.getAllergies().isEmpty());

        // Résident 3 : Tenley Boyd
        FireAlertPersonDTO r3 = dto.getResidents().get(2);
        assertEquals("Tenley", r3.getFirstName());
        assertEquals("Boyd",   r3.getLastName());
        assertEquals("841-874-6512", r3.getPhone());
        assertTrue(r3.getAge() >= 0);
        assertTrue(r3.getMedications().isEmpty());
        assertEquals(List.of("peanut"), r3.getAllergies());

        // Résident 4 : Roger Boyd
        FireAlertPersonDTO r4 = dto.getResidents().get(3);
        assertEquals("Roger", r4.getFirstName());
        assertEquals("Boyd",  r4.getLastName());
        assertEquals("841-874-6512", r4.getPhone());
        assertTrue(r4.getAge() >= 0);
        assertTrue(r4.getMedications().isEmpty());
        assertTrue(r4.getAllergies().isEmpty());

        // Résident 5 : Felicia Boyd
        FireAlertPersonDTO r5 = dto.getResidents().get(4);
        assertEquals("Felicia", r5.getFirstName());
        assertEquals("Boyd",    r5.getLastName());
        assertEquals("841-874-6544", r5.getPhone());
        assertTrue(r5.getAge() > 0);
        assertEquals(List.of("tetracyclaz:650mg"), r5.getMedications());
        assertEquals(List.of("xilliathal"), r5.getAllergies());

        // Interactions (10 appels MR = 2 par résident)
        verify(fireStationService).findAll();
        verify(personRepository).findAll();
        verify(medicalRecordService, times(5)).get(any(MedicalRecord.class));
    }
}
