package com.openclassrooms.SafetyNetAlerts.controller;



import java.util.List;

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
	public void createPerson(@RequestBody Person person) {
		logger.info("createPerson called");
		personService.add(person);
		logger.info("Person successfully created" + person.toString());
	}
	
	@DeleteMapping("/person")
	public void deletePerson(@RequestBody Person person) {
		logger.info("deletePerson called");
		personService.delete(person);
		logger.info("Person successfully deleted" + person.toString());
	}
	
	@PutMapping("/person")
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
}
