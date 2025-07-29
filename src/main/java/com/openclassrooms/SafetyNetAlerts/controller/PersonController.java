package com.openclassrooms.SafetyNetAlerts.controller;



import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.openclassrooms.SafetyNetAlerts.model.Person;
import com.openclassrooms.SafetyNetAlerts.service.impl.PersonServiceImpl;



@RestController
@RequestMapping("/person")
public class PersonController {
	private static Logger logger = LogManager.getLogger(PersonController.class);
	
	@Autowired
	private PersonServiceImpl personService;
	
	
	@PostMapping("")
	public void createPerson(Person person) {
		logger.info("createPerson called");
		personService.add(person);
		logger.info("Person successfully created" + person.toString());
	}
	
	@DeleteMapping("")
	public void deletePerson(Person person) {
		logger.info("deletePerson called");
		personService.delete(person);
		logger.info("Person successfully deleted" + person.toString());
	}
	
	@PutMapping("")
	public void updatePerson(Person person) {
		logger.info("updatePerson called");
		personService.update(person);
		logger.info("Person successfully updated" + person.toString());
	}
	
	@GetMapping("")
	public Person getPerson(Person person) {
		logger.info("getPerson called");
		personService.get(person);
		logger.info("Person successfully gotten" + person.toString());
		return person;
	}
	
	@GetMapping("/all")
    public List<Person> getPersonList() {
        logger.info("getPersonList called");
        List<Person> personList = personService.findAll();
        logger.info("Person list successfully gotten" + personList.toString());
        return personList;
    }
}
