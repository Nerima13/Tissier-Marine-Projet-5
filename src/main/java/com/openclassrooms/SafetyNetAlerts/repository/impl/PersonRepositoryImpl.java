package com.openclassrooms.SafetyNetAlerts.repository.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.openclassrooms.SafetyNetAlerts.model.Person;
import com.openclassrooms.SafetyNetAlerts.repository.PersonRepository;

@Repository
public class PersonRepositoryImpl implements PersonRepository {
	
	private static Logger logger = LogManager.getLogger(PersonRepositoryImpl.class);
	private List<Person> persons = new ArrayList<>();
	
	@Override
	public void add(Person element) {
		persons.add(element);
	}
	
	@Override
	public void delete(Person element) {
		persons.remove(element);
	}
	
	@Override
	public void update(Person element) {
	    persons.forEach(p -> {
	    	if (p.equals(element)) {
	    		p.setAddress(element.getAddress());
	            p.setCity(element.getCity());
	            p.setZip(element.getZip());
	            p.setPhone(element.getPhone());
	            p.setEmail(element.getEmail());
	        }
	    });
	}
	
	@Override
	public Person get(Person element) {
		for (Person p : persons) {
			if (p.equals(element)) {
				return p;
			}
		}
		return null;
	}
	
	@Override
	public List<Person> findAll() {
		return persons;
	}

}
