package com.openclassrooms.SafetyNetAlerts.repository;

import com.openclassrooms.SafetyNetAlerts.model.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class PersonRepositoryTest {

    @Autowired
    @Qualifier("personRepoSingleton")
    private PersonRepository personRepository;

    @BeforeEach
    public void setUp() {
        personRepository.clear();
    }

    @Test
    public void addPersonTest() {
        // Arrange
        Person person = new Person();
        person.setFirstName("firstName");
        person.setLastName("lastName");
        int oldSize = personRepository.findAll().size();

        // Act
        personRepository.add(person);

        // Assert
        List<Person> list = personRepository.findAll();
        assertEquals(oldSize + 1, list.size()); // the size has increased
        assertEquals(person, list.get(list.size() - 1)); // the last element is the one added
    }

    @Test
    public void deletePersonTest() {
        // Arrange
        Person person = new Person();
        person.setFirstName("firstName");
        person.setLastName("lastName");
        personRepository.add(person);
        int oldSize = personRepository.findAll().size();

        // Act
        personRepository.delete(person);

        // Assert
        List<Person> list = personRepository.findAll();
        assertEquals(oldSize - 1, list.size()); // the size has decreased

        // Check that the person is no longer found
        Person deletedPerson = new Person();
        deletedPerson.setFirstName("firstName");
        deletedPerson.setLastName("lastName");
        assertFalse(list.contains(deletedPerson));
    }

    @Test
    public void updatePersonTest() {
        // Arrange
        Person personAdd = new Person();
        personAdd.setFirstName("firstName");
        personAdd.setLastName("lastName");
        personRepository.add(personAdd);

        Person personUpdate = new Person();
        personUpdate.setFirstName("firstName");
        personUpdate.setLastName("lastName");
        personUpdate.setAddress("address");

        // Act
        personRepository.update(personUpdate);

        // Assert
        List<Person> list = personRepository.findAll();
        assertTrue(list.contains(personAdd)); // check that the element still exists
        assertEquals("address", personAdd.getAddress()); // the address has been updated
    }
}
