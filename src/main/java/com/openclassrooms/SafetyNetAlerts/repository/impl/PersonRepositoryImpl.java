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
        logger.info("Repo add person {} {}", person.getFirstName(), person.getLastName());
        logger.debug("Add payload: {}", person);
        try {
            persons.add(person);
            logger.info("Repo add -> OK (size={})", persons.size());
        } catch (Exception e) {
            logger.error("Repo add FAILED for {} {}: {}",
                    person.getFirstName(), person.getLastName(), e.getMessage(), e);
            throw e;
        }
    }
	
	@Override
	public void delete(Person person) {
        logger.info("Repo delete person {} {}", person.getFirstName(), person.getLastName());
        try {
            boolean removed = persons.remove(person);
            if (removed) {
                logger.info("Repo delete -> OK (size={})", persons.size());
            } else {
                logger.error("Repo delete -> person not found: {} {}",
                        person.getFirstName(), person.getLastName());
            }
        } catch (Exception e) {
            logger.error("Repo delete FAILED for {} {}: {}",
                    person.getFirstName(), person.getLastName(), e.getMessage(), e);
            throw e;
        }
    }
	
	@Override
	public void update(Person person) {
        logger.info("Repo update person {} {}", person.getFirstName(), person.getLastName());
        logger.debug("Update payload: {}", person);
        try {
            boolean updated = false;
            for (Person p : persons) {
                if (p.equals(person)) {
                    p.setAddress(person.getAddress());
                    p.setCity(person.getCity());
                    p.setZip(person.getZip());
                    p.setPhone(person.getPhone());
                    p.setEmail(person.getEmail());
                    updated = true;
                    break;
                }
            }
            if (updated) {
                logger.info("Repo update -> OK for {} {}", person.getFirstName(), person.getLastName());
                logger.debug("Repo update result: {}", person);
            } else {
                logger.error("Repo update -> person not found: {} {}",
                        person.getFirstName(), person.getLastName());
            }
        } catch (Exception e) {
            logger.error("Repo update FAILED for {} {}: {}",
                    person.getFirstName(), person.getLastName(), e.getMessage(), e);
            throw e;
        }
    }
	
	@Override
	public Person get(Person person) {
        logger.debug("Repo get person {} {}", person.getFirstName(), person.getLastName());
        try {
            for (Person p : persons) {
                if (p.equals(person)) {
                    logger.info("Repo get -> found {} {}", person.getFirstName(), person.getLastName());
                    logger.debug("Repo get result: {}", p);
                    return p;
                }
            }
            logger.info("Repo get -> result=empty ({} {})",
                    person.getFirstName(), person.getLastName());
            return null;
        } catch (Exception e) {
            logger.error("Repo get FAILED for {} {}: {}",
                    person.getFirstName(), person.getLastName(), e.getMessage(), e);
            throw e;
        }
    }
	
	@Override
	public List<Person> findAll() {
        logger.info("Repo findAll persons");
        try {
            logger.info("Repo findAll -> count={}", persons.size());
            logger.debug("Repo findAll sample: {}", persons.stream().limit(3).toList());
            return persons;
        } catch (Exception e) {
            logger.error("Repo findAll FAILED: {}", e.getMessage(), e);
            throw e;
        }
    }
	
	@Override
	public void addAll(List<Person> element) {
        try {
            if (element != null) {
                persons.addAll(element);
            }
            logger.info("Initial data loaded: persons={}", persons.size());
        } catch (Exception e) {
            logger.error("Repo add all FAILED: {}", e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public void clear() {
        logger.debug("Repo clear persons ({} -> 0)", persons.size());
        persons.clear();
        logger.info("Repo clear -> OK");
    }
}
