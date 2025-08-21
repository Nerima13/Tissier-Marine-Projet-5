package com.openclassrooms.SafetyNetAlerts.repository.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import com.openclassrooms.SafetyNetAlerts.model.Person;
import com.openclassrooms.SafetyNetAlerts.repository.PersonRepository;

@Repository
public class PersonRepositoryImpl implements PersonRepository {
	
	private static Logger logger = LogManager.getLogger(PersonRepositoryImpl.class);
	private final List<Person> persons = new ArrayList<>();
	
	@Bean
	@Scope("singleton")
	public PersonRepositoryImpl personRepoSingleton() {
		return new PersonRepositoryImpl();
	}
		
	@Override
	public void add(Person person) {
		persons.add(person);
	}
	
	@Override
	public void delete(Person person) {
		persons.remove(person);
	}
	
	@Override
	public void update(Person person) {
	    persons.forEach(p -> {
	    	if (p.equals(person)) {
	    		p.setAddress(person.getAddress());
	            p.setCity(person.getCity());
	            p.setZip(person.getZip());
	            p.setPhone(person.getPhone());
	            p.setEmail(person.getEmail());
	        }
	    });
	}
	
	@Override
	public Person get(Person person) {
		for (Person p : persons) {
			if (p.equals(person)) {
				return p;
			}
		}
		return null;
	}
	
	@Override
	public List<Person> findAll() {
		return persons;
	}
	
	@Override
	public void addAll(List<Person> element) {
		persons.addAll(element);
	}

    @Override
    public void clear() {
        persons.clear();
    }
}
