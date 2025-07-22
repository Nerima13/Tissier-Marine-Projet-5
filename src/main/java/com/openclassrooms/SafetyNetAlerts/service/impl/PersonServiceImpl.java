package com.openclassrooms.SafetyNetAlerts.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.openclassrooms.SafetyNetAlerts.model.Person;
import com.openclassrooms.SafetyNetAlerts.repository.PersonRepository;
import com.openclassrooms.SafetyNetAlerts.service.PersonService;

@Service
public class PersonServiceImpl implements PersonService {

	@Autowired
    private PersonRepository personRepository;

    @Override
    public void add(Person element) {
        personRepository.add(element);
    }

    @Override
    public void delete(Person element) {
        personRepository.delete(element);
    }

    @Override
    public void update(Person element) {
        personRepository.update(element);
    }

    @Override
    public Person get(Person element) {
        return personRepository.get(element); 
    }

    @Override
    public List<Person> findAll() {
        return personRepository.findAll();
    }
}
