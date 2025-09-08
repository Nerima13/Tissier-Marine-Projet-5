package com.openclassrooms.SafetyNetAlerts.repository;

import com.openclassrooms.SafetyNetAlerts.model.Person;
import com.openclassrooms.SafetyNetAlerts.repository.impl.PersonRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PersonRepositoryImplTest {

    private PersonRepositoryImpl repository;

    private Person person(String firstName, String lastName, String address, String city, String zip, String phone, String email) {
        Person p = new Person();
        p.setFirstName(firstName);
        p.setLastName(lastName);
        p.setAddress(address);
        p.setCity(city);
        p.setZip(zip);
        p.setPhone(phone);
        p.setEmail(email);
        return p;
    }

    @BeforeEach
    public void setUp() {
        repository = new PersonRepositoryImpl();
    }

    @Test
    public void addPersonTest() {
        Person p = person("John","Boyd","1509 Culver St","Culver","97451","841-874-6512","jaboyd@email.com");

        repository.add(p);

        assertEquals(1, repository.findAll().size());
        assertNotNull(repository.get(p));
    }

    @Test
    public void deletePersonTest() {
        Person p = person("John","Boyd","1509 Culver St","Culver","97451","841-874-6512","jaboyd@email.com");
        repository.add(p);

        repository.delete(p);

        assertEquals(0, repository.findAll().size());
        assertNull(repository.get(p));
    }

    @Test
    public void updatePersonTest() {
        Person p = person("John","Boyd","1509 Culver St","Culver","97451","841-874-6512","jaboyd@email.com");
        repository.add(p);

        Person modified = person("John","Boyd","New Address","Culver","97451","841-874-6512","jaboyd@email.com");
        repository.update(modified);

        Person saved = repository.get(p);
        assertNotNull(saved);
        assertEquals("New Address", saved.getAddress());
    }

    @Test
    public void findAllPersonsTest() {
        Person p1 = person("John","Boyd","1509 Culver St","Culver","97451","841-874-6512","jaboyd@email.com");
        Person p2 = person("Peter", "Duncan", "644 Gershwin Cir", "Culver", "97451", "841-874-6512", "jaboyd@email.com");

        repository.add(p1);
        repository.add(p2);

        assertEquals(2, repository.findAll().size());
        assertNotNull(repository.get(p1));
        assertNotNull(repository.get(p2));
    }

    @Test
    public void clearPersonTest() {
        Person p1 = person("John","Boyd","1509 Culver St","Culver","97451","841-874-6512","jaboyd@email.com");
        Person p2 = person("Peter", "Duncan", "644 Gershwin Cir", "Culver", "97451", "841-874-6512", "jaboyd@email.com");
        repository.add(p1);
        repository.add(p2);

        repository.clear();

        assertTrue(repository.findAll().isEmpty());
        assertNull(repository.get(p1));
        assertNull(repository.get(p2));
    }
}
