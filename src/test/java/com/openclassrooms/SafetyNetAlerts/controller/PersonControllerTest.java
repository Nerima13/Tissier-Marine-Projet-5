package com.openclassrooms.SafetyNetAlerts.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.SafetyNetAlerts.dto.request.childAlert.ChildDTO;
import com.openclassrooms.SafetyNetAlerts.dto.request.childAlert.FamilyMemberDTO;
import com.openclassrooms.SafetyNetAlerts.dto.request.fireAlert.FireAlertAddressDTO;
import com.openclassrooms.SafetyNetAlerts.dto.request.fireAlert.FireAlertPersonDTO;
import com.openclassrooms.SafetyNetAlerts.dto.request.firestationNumber.FireStationCoverageDTO;
import com.openclassrooms.SafetyNetAlerts.dto.request.firestationNumber.PersonByStationNumberDTO;
import com.openclassrooms.SafetyNetAlerts.dto.request.floodAlert.FloodAlertAddressDTO;
import com.openclassrooms.SafetyNetAlerts.dto.request.floodAlert.FloodAlertDTO;
import com.openclassrooms.SafetyNetAlerts.dto.request.floodAlert.FloodAlertPersonDTO;
import com.openclassrooms.SafetyNetAlerts.dto.request.personInfo.PersonInfoDTO;
import com.openclassrooms.SafetyNetAlerts.model.Person;
import com.openclassrooms.SafetyNetAlerts.service.impl.PersonServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PersonController.class)
public class PersonControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PersonServiceImpl personService;

    @Test
    public void createPersonTest() throws Exception {
        Person p = new Person("John","Boyd","1509 Culver St","Culver","97451","841-874-6512", "jaboyd@email.com");

        mockMvc.perform(post("/person")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(p)))
                .andExpect(status().isCreated());

        verify(personService).add(p);
    }

    @Test
    public void deletePersonTest() throws Exception {
        Person p = new Person("John","Boyd","1509 Culver St","Culver","97451","841-874-6512", "jaboyd@email.com");

        mockMvc.perform(delete("/person")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(p)))
                .andExpect(status().isOk());

        verify(personService).delete(p);
    }

    @Test
    public void updatePersonTest() throws Exception {
        Person p = new Person("John","Boyd","1509 Culver St","Culver","97451","841-874-6512", "jaboyd@email.com");

        mockMvc.perform(put("/person")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(p)))
                .andExpect(status().isOk());

        verify(personService).update(any(Person.class));
    }

    @Test
    public void getPersonTest() throws Exception {
        Person p = new Person("John","Boyd","1509 Culver St","Culver","97451","841-874-6512", "jaboyd@email.com");

        when(personService.get(any(Person.class))).thenReturn(p);

        mockMvc.perform(get("/person?firstName=John&lastName=Boyd"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Boyd"));

        verify(personService).get(any(Person.class));
    }

    @Test
    public void getPersonListTest() throws Exception {
        Person p1 = new Person();
        p1.setFirstName("John");
        p1.setLastName("Boyd");
        Person p2 = new Person();
        p2.setFirstName("Jacob");
        p2.setLastName("Boyd");

        when(personService.findAll()).thenReturn(List.of(p1, p2));

        mockMvc.perform(get("/persons"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].firstName").value("John"))
                .andExpect(jsonPath("$[1].firstName").value("Jacob"));

        verify(personService).findAll();
    }

    @Test
    public void getPersonsCoveredByStationTest() throws Exception {
        PersonByStationNumberDTO p1 = new PersonByStationNumberDTO();
        p1.setFirstName("John");
        p1.setLastName("Boyd");
        p1.setAddress("1509 Culver St");
        p1.setPhone("841-874-6512");

        PersonByStationNumberDTO p2 = new PersonByStationNumberDTO();
        p2.setFirstName("Tenley");
        p2.setLastName("Boyd");
        p1.setAddress("1509 Culver St");
        p2.setPhone("841-874-6512");

        FireStationCoverageDTO dto = new FireStationCoverageDTO();
        dto.setStation("3");
        dto.setInfoPerson(List.of(p1, p2));
        dto.setNbAdult(1);
        dto.setNbChild(1);

        when(personService.getPersonsCoveredByStation("3")).thenReturn(dto);

        mockMvc.perform(get("/firestationNumber?stationNumber=3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nbAdult").value(1))
                .andExpect(jsonPath("$.nbChild").value(1))
                .andExpect(jsonPath("$.infoPerson", hasSize(2)))
                .andExpect(jsonPath("$.infoPerson[*].firstName", hasItems("John","Tenley")));

        verify(personService).getPersonsCoveredByStation("3");
    }

    @Test
    public void getChildInfosTest() throws Exception {
        ChildDTO child = new ChildDTO();
        child.setFirstName("Tenley");
        child.setLastName("Boyd");
        child.setAge(13);
        child.setFamily(List.of(new FamilyMemberDTO("John", "Boyd")));

        when(personService.getChildInfos("1509 Culver St")).thenReturn(List.of(child));

        mockMvc.perform(get("/childAlert?address=1509 Culver St"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].firstName").value("Tenley"))
                .andExpect(jsonPath("$[0].lastName").value("Boyd"))
                .andExpect(jsonPath("$[0].age").value(13))
                .andExpect(jsonPath("$[0].family", hasSize(1)))
                .andExpect(jsonPath("$[0].family[0].firstName").value("John"))
                .andExpect(jsonPath("$[0].family[0].lastName").value("Boyd"));

        verify(personService).getChildInfos("1509 Culver St");
    }

    @Test
    public void getPhoneByFireStationTest() throws Exception {
        List<String> phones = List.of("841-874-6512", "841-874-6513");

        when(personService.getPhoneByFireStation("3")).thenReturn(phones);

        mockMvc.perform(get("/phoneAlert?firestation=3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0]").value("841-874-6512"))
                .andExpect(jsonPath("$[1]").value("841-874-6513"));

        verify(personService).getPhoneByFireStation("3");
    }

    @Test
    public void getFireAlertTest() throws Exception {
        FireAlertPersonDTO p1 = new FireAlertPersonDTO();
        p1.setFirstName("John");
        p1.setLastName("Boyd");
        p1.setAge(41);
        p1.setPhone("841-874-6512");
        p1.setMedications(List.of("aznol:350mg", "hydrapermazol:100mg"));
        p1.setAllergies(List.of("nillacilan"));

        FireAlertAddressDTO dto = new FireAlertAddressDTO();
        dto.setStation("3");
        dto.setResidents(List.of(p1));

        when(personService.getFireAlert("1509 Culver St")).thenReturn(dto);

        mockMvc.perform(get("/fire?address=1509 Culver St"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.station").value("3"))
                .andExpect(jsonPath("$.residents", hasSize(1)))
                .andExpect(jsonPath("$.residents[0].firstName").value("John"))
                .andExpect(jsonPath("$.residents[0].age").value(41))
                .andExpect(jsonPath("$.residents[0].phone").value("841-874-6512"))
                .andExpect(jsonPath("$.residents[0].medications", hasSize(2)))
                .andExpect(jsonPath("$.residents[0].medications[0]").value("aznol:350mg"))
                .andExpect(jsonPath("$.residents[0].medications[1]").value("hydrapermazol:100mg"))
                .andExpect(jsonPath("$.residents[0].allergies", hasSize(1)))
                .andExpect(jsonPath("$.residents[0].allergies[0]").value("nillacilan"));

        verify(personService).getFireAlert("1509 Culver St");
    }

    @Test
    public void getFloodAlertTest() throws Exception {
        FloodAlertPersonDTO p = new FloodAlertPersonDTO();
        p.setFirstName("Eric");
        p.setLastName("Cadigan");
        p.setPhone("841-874-7458");
        p.setAge(80);
        p.setMedications(List.of("tradoxidine:400mg"));

        FloodAlertAddressDTO addr = new FloodAlertAddressDTO();
        addr.setAddress("951 LoneTree Rd");
        addr.getListPerson().add(p);

        FloodAlertDTO dto = new FloodAlertDTO();
        dto.setStations(List.of("2"));
        dto.setAddressList(List.of(addr));

        when(personService.getFloodAlert(List.of("2"))).thenReturn(dto);

        mockMvc.perform(get("/flood/stations?stations=2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.stations", hasSize(1)))
                .andExpect(jsonPath("$.stations[0]").value("2"))
                .andExpect(jsonPath("$.addressList", hasSize(1)))
                .andExpect(jsonPath("$.addressList[0].address").value("951 LoneTree Rd"))
                .andExpect(jsonPath("$.addressList[0].listPerson", hasSize(1)))
                .andExpect(jsonPath("$.addressList[0].listPerson[0].firstName").value("Eric"))
                .andExpect(jsonPath("$.addressList[0].listPerson[0].phone").value("841-874-7458"))
                .andExpect(jsonPath("$.addressList[0].listPerson[0].medications[0]").value("tradoxidine:400mg"));

        verify(personService).getFloodAlert(List.of("2"));
    }

    @Test
    public void getPersonInfoTest() throws Exception {
        PersonInfoDTO john = new PersonInfoDTO();
        john.setFirstName("John");
        john.setLastName("Boyd");
        john.setAge(41);
        john.setAddress("1509 Culver St");
        john.setEmail("jaboyd@email.com");
        john.setMedications(List.of("aznol:350mg", "hydrapermazol:100mg"));
        john.setAllergies(List.of("nillacilan"));

        when(personService.getPersonInfo("Boyd")).thenReturn(List.of(john));

        mockMvc.perform(get("/personInfolastName").param("lastName", "Boyd"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].firstName").value("John"))
                .andExpect(jsonPath("$[0].lastName").value("Boyd"))
                .andExpect(jsonPath("$[0].age").value(41))
                .andExpect(jsonPath("$[0].address").value("1509 Culver St"))
                .andExpect(jsonPath("$[0].email").value("jaboyd@email.com"))
                .andExpect(jsonPath("$[0].medications", hasSize(2)))
                .andExpect(jsonPath("$[0].medications[0]").value("aznol:350mg"))
                .andExpect(jsonPath("$[0].allergies[0]").value("nillacilan"));

        verify(personService).getPersonInfo("Boyd");
    }

    @Test
    public void getEmailTest() throws Exception {
        List<String> emails = List.of(
                "jaboyd@email.com",
                "drk@email.com");

        when(personService.getEmail("Culver")).thenReturn(emails);

        mockMvc.perform(get("/communityEmail").param("city", "Culver"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0]").value("jaboyd@email.com"))
                .andExpect(jsonPath("$[1]").value("drk@email.com"));

        verify(personService).getEmail("Culver");
    }
}
