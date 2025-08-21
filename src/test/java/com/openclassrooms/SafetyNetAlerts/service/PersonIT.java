package com.openclassrooms.SafetyNetAlerts.service;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc

public class PersonIT {

    @Autowired
    public MockMvc mockMvc;

    @Test
    public void getPersonTest() throws Exception {
        mockMvc.perform(get("/person?firstName=John&lastName=Boyd"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is("John")))
                .andExpect(jsonPath("$.lastName", is("Boyd")))
                .andExpect(jsonPath("$.address", is("1509 Culver St")))
                .andExpect(jsonPath("$.city", is("Culver")))
                .andExpect(jsonPath("$.zip", is(97451)))
                .andExpect(jsonPath("$.phone", is("841-874-6512")))
                .andExpect(jsonPath("$.city", is("Culver")))
                .andExpect(jsonPath("$.email", is("jaboyd@email.com")));
    }

    @Test
    public void getPersonsCoveredByStationTest() throws Exception {
        mockMvc.perform(get("/firestationNumber?stationNumber=3"))
                .andExpect(status().isOk())

                .andExpect(jsonPath("$.station", is("3")))
                .andExpect(jsonPath("$.nbAdult", is(8)))
                .andExpect(jsonPath("$.nbChild", is(3)))
                .andExpect(jsonPath("$.infoPerson", hasSize(11)))
                .andExpect(jsonPath("$.infoPerson[0].firstName", is("John")))
                .andExpect(jsonPath("$.infoPerson[0].lastName", is("Boyd")))
                .andExpect(jsonPath("$.infoPerson[0].address", is("1509 Culver St")))
                .andExpect(jsonPath("$.infoPerson[0].phone", is("841-874-6512")));
    }

    @Test
    public void getChildInfosTest() throws Exception {
        mockMvc.perform(get("/childAlert?address=1509 Culver St"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))

                .andExpect(jsonPath("$[0].firstName", is("Tenley")))
                .andExpect(jsonPath("$[0].lastName", is("Boyd")))
                .andExpect(jsonPath("$[0].age", is(13)))
                .andExpect(jsonPath("$[0].family", hasSize(4)))
                .andExpect(jsonPath("$[0].family[*].firstName", containsInAnyOrder("John","Jacob","Roger","Felicia")))
                .andExpect(jsonPath("$[0].family[*].lastName", everyItem(is("Boyd"))));
    }

    @Test
    public void getPhoneByFireStationTest() throws Exception {
        mockMvc.perform(get("/phoneAlert?firestation=3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", containsInAnyOrder("841-874-6512", "841-874-6513",
                        "841-874-6512", "841-874-6512", "841-874-6544",
                        "841-874-6512", "841-874-6544", "841-874-6874",
                        "841-874-8888", "841-874-9888", "841-874-6741")));
    }

    @Test
    public void getFireAlertTest() throws Exception {
        mockMvc.perform(get("/fire?address=1509 Culver St"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.station", is("3")))
                .andExpect(jsonPath("$.residents", hasSize(5)))

                .andExpect(jsonPath("$.residents[0].firstName", is("John")))
                .andExpect(jsonPath("$.residents[0].lastName", is("Boyd")))
                .andExpect(jsonPath("$.residents[0].age", is(41)))
                .andExpect(jsonPath("$.residents[0].phone", is("841-874-6512")))
                .andExpect(jsonPath("$.residents[0].medications", hasSize(2)))
                .andExpect(jsonPath("$.residents[0].medications[0]", is("aznol:350mg")))
                .andExpect(jsonPath("$.residents[0].medications[1]", is("hydrapermazol:100mg")))
                .andExpect(jsonPath("$.residents[0].allergies", hasSize(1)))
                .andExpect(jsonPath("$.residents[0].allergies[0]", is("nillacilan")));

    }

    @Test
    public void getFloodAlertTest() throws Exception {
        mockMvc.perform(get("/flood/stations?stations=1,2,3"))
                .andExpect(status().isOk())

                .andExpect(jsonPath("$.stations", hasSize(3)))
                .andExpect(jsonPath("$.stations[0]", is("1")))
                .andExpect(jsonPath("$.stations[1]", is("2")))
                .andExpect(jsonPath("$.stations[2]", is("3")))

                .andExpect(jsonPath("$.addressList", hasSize(10)))
                .andExpect(jsonPath("$.addressList[0].address", is("644 Gershwin Cir")))

                .andExpect(jsonPath("$.addressList[0].listPerson", hasSize(1)))
                .andExpect(jsonPath("$.addressList[0].listPerson[0].firstName", is("Peter")))
                .andExpect(jsonPath("$.addressList[0].listPerson[0].lastName", is("Duncan")))
                .andExpect(jsonPath("$.addressList[0].listPerson[0].phone", is("841-874-6512")))
                .andExpect(jsonPath("$.addressList[0].listPerson[0].age", is(24)))
                .andExpect(jsonPath("$.addressList[0].listPerson[0].medications", hasSize(0)))
                .andExpect(jsonPath("$.addressList[0].listPerson[0].allergies", hasSize(1)))
                .andExpect(jsonPath("$.addressList[0].listPerson[0].allergies[0]", is("shellfish")));
    }

    @Test
    public void getPersonInfoTest() throws Exception {
        mockMvc.perform(get("/personInfolastName?lastName=Boyd"))
                .andExpect(status().isOk())

                .andExpect(jsonPath("$[0].firstName", is("John")))
                .andExpect(jsonPath("$[0].lastName", is("Boyd")))
                .andExpect(jsonPath("$[0].address", is("1509 Culver St")))
                .andExpect(jsonPath("$[0].age", is(41)))
                .andExpect(jsonPath("$[0].email", is("jaboyd@email.com")))
                .andExpect(jsonPath("$[0].medications", hasSize(2)))
                .andExpect(jsonPath("$[0].medications[0]", is("aznol:350mg")))
                .andExpect(jsonPath("$[0].medications[1]", is("hydrapermazol:100mg")))
                .andExpect(jsonPath("$[0].allergies", hasSize(1)))
                .andExpect(jsonPath("$[0].allergies[0]", is("nillacilan")));
    }

    @Test
    public void getEmailTest() throws Exception {
        mockMvc.perform(get("/communityEmail?city=Culver"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0]", is("jaboyd@email.com")))
                .andExpect(jsonPath("$[0]", containsString("@")));
    }

}
