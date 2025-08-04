package com.openclassrooms.SafetyNetAlerts.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.openclassrooms.SafetyNetAlerts.model.Person;
import com.openclassrooms.SafetyNetAlerts.repository.PersonRepository;
import com.openclassrooms.SafetyNetAlerts.service.PersonService;

@Service
public class PersonServiceImpl implements PersonService {

	@Autowired
	@Qualifier("personRepoSingleton")
    private PersonRepository personRepository;

    @Override
    public void add(Person person) {
        personRepository.add(person);
    }

    @Override
    public void delete(Person person) {
        personRepository.delete(person);
    }

    @Override
    public void update(Person person) {
        personRepository.update(person);
    }

    @Override
    public Person get(Person person) {
        return personRepository.get(person);
    }

    @Override
    public List<Person> findAll() {
        return personRepository.findAll();
    }
}
