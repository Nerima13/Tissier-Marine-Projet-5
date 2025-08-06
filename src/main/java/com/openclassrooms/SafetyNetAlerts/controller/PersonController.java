package com.openclassrooms.SafetyNetAlerts.controller;



import java.util.List;

import com.openclassrooms.SafetyNetAlerts.dto.request.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.openclassrooms.SafetyNetAlerts.model.Person;
import com.openclassrooms.SafetyNetAlerts.service.impl.PersonServiceImpl;



@RestController
public class PersonController {
	private static Logger logger = LogManager.getLogger(PersonController.class);
	
	@Autowired
	private PersonServiceImpl personService;
	
	
	@PostMapping("/person")
	@ResponseStatus(code = HttpStatus.CREATED)
	public void createPerson(@RequestBody Person person) {
		logger.info("createPerson called");
		personService.add(person);
		logger.info("Person successfully created" + person.toString());
	}
	
	@DeleteMapping("/person")
	@ResponseStatus(code = HttpStatus.OK)
	public void deletePerson(@RequestBody Person person) {
		logger.info("deletePerson called");
		personService.delete(person);
		logger.info("Person successfully deleted" + person.toString());
	}
	
	@PutMapping("/person")
	@ResponseStatus(code = HttpStatus.OK)
	public void updatePerson(@RequestBody Person person) {
		logger.info("updatePerson called");
		personService.update(person);
		logger.info("Person successfully updated" + person.toString());
	}

	@GetMapping("/person")
	@ResponseStatus(code = HttpStatus.OK)
	public Person getPerson(@RequestParam("firstName") String firstName,
							@RequestParam("lastName") String lastName) {
		logger.info("getPerson called");
		Person person = new Person(firstName, lastName);
		person = personService.get(person);
		logger.info("getPerson response : " + person.toString());
		return person;
	}
	
	@GetMapping("/persons")
    public List<Person> getPersonList() {
        logger.info("getPersonList called");
        List<Person> personList = personService.findAll();
        logger.info("getPersonList response : " + personList.toString());
        return personList;
    }

    @GetMapping("/firestation")
    public FireStationCoverageDTO getPersonsCoveredByStation(@RequestParam("station") int station) {
        logger.info("getPersonsCoveredByStation called");
		return personService.getPersonsCoveredByStation(station);
    }

    @GetMapping("/childAlert")
    public ChildDTO getChildInfos(@RequestParam("address") String address) {
		return personService.getChildInfos(address);
    }

    @GetMapping("/phoneAlert")
    public List<String> getPhoneByFireStation(@RequestParam("firestation") String fireStationNumber) {
		return personService.getPhoneByFireStation(fireStationNumber);
    }

    @GetMapping("/fire")
    public FireAlertDTO getFireAlert(@RequestParam("address") String address) {
		return personService.getFireAlert(address);
    }

    @GetMapping("/flood/stations")
    public FloodAlertDTO getFloodAlert(@RequestParam("stations") List<String> stations) {
		return personService.getFloodAlert(stations);
    }

    @GetMapping("/personInfo")
    public PersonInfoDTO getPersonInfo(@RequestParam("firstName") String firstName,
                                       @RequestParam("lastName") String lastName) {
		return personService.getPersonInfo(firstName, lastName);
    }

    @GetMapping("/communityEmail")
    public List<String> getEmail(@RequestParam("city") String city) {
		return personService.getEmail(city);
    }
}
