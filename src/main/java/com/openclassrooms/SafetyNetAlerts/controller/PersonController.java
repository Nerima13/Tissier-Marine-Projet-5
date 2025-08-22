package com.openclassrooms.SafetyNetAlerts.controller;

import java.util.List;

import com.openclassrooms.SafetyNetAlerts.dto.request.childAlert.ChildDTO;
import com.openclassrooms.SafetyNetAlerts.dto.request.fireAlert.FireAlertAddressDTO;
import com.openclassrooms.SafetyNetAlerts.dto.request.firestationNumber.FireStationCoverageDTO;
import com.openclassrooms.SafetyNetAlerts.dto.request.floodAlert.FloodAlertDTO;
import com.openclassrooms.SafetyNetAlerts.dto.request.personInfo.PersonInfoDTO;
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
        logger.info("POST /person - creating {} {}", person.getFirstName(), person.getLastName());
        logger.debug("Payload : {}", person);
        try {
            personService.add(person);
            logger.info("POST /person -> 201 Created");
        } catch (Exception e) {
            logger.error("POST /person FAILED for {} {}: {}", person.getFirstName(), person.getLastName(), e.getMessage(), e);
            throw e;
        }
    }
	
	@DeleteMapping("/person")
	@ResponseStatus(code = HttpStatus.OK)
	public void deletePerson(@RequestBody Person person) {
        logger.info("DELETE /person - {} {}", person.getFirstName(), person.getLastName());
        try {
            personService.delete(person);
            logger.info("DELETE /person -> 200 OK");
        } catch (Exception e) {
            logger.error("DELETE /person FAILED for {} {}: {}", person.getFirstName(), person.getLastName(), e.getMessage(), e);
            throw e;
        }
	}
	
	@PutMapping("/person")
	@ResponseStatus(code = HttpStatus.OK)
	public void updatePerson(@RequestBody Person person) {
        logger.info("PUT /person - {} {}",
                person.getFirstName(), person.getLastName());
        logger.debug("Payload : {}", person);
        try {
            personService.update(person);
            logger.info("PUT /person -> 200 OK");
        } catch (Exception e) {
            logger.error("PUT /person FAILED for {} {}: {}",
                    person.getFirstName(), person.getLastName(), e.getMessage(), e);
            throw e;
        }
	}

	@GetMapping("/person")
	@ResponseStatus(code = HttpStatus.OK)
	public Person getPerson(@RequestParam("firstName") String firstName,
							@RequestParam("lastName") String lastName) {
        logger.info("GET /person firstName='{}' lastName='{}'", firstName, lastName);
        Person p = personService.get(new Person(firstName, lastName));
        if (p == null) {
            logger.info("GET /person -> 200 OK, result=empty ({} {})", firstName, lastName);
        } else {
            logger.info("GET /person -> 200 OK");
            logger.debug("Result person: {}", p);
        }
        return p;
    }
	
	@GetMapping("/persons")
    public List<Person> getPersonList() {
        logger.info("GET /persons");
        List<Person> list = personService.findAll();
        logger.info("GET /persons -> 200 OK, count={}", list.size());
        logger.debug("First results: {}",
                list.stream().limit(3).toList());
        return list;
    }

    @GetMapping("/firestationNumber")
    public FireStationCoverageDTO getPersonsCoveredByStation(@RequestParam("stationNumber") String station) {
        logger.info("GET /firestation stationNumber={}", station);
        try {
            FireStationCoverageDTO dto = personService.getPersonsCoveredByStation(station);
            logger.info("GET /firestation -> 200 OK, residents={}, adults={}, children={}",
                    dto.getInfoPerson().size(), dto.getNbAdult(), dto.getNbChild());
            logger.debug("Sample phones: {}",
                    dto.getInfoPerson().stream().map(p -> p.getPhone()).limit(3).toList());
            return dto;
        } catch (Exception e) {
            logger.error("GET /firestationNumber FAILED for station {}: {}", station, e.getMessage(), e);
            throw e;
        }
    }

    @GetMapping("/childAlert")
    public List<ChildDTO> getChildInfos(@RequestParam("address") String address) {
        logger.info("GET /childAlert?address={}", address);
        try {
            List<ChildDTO> children = personService.getChildInfos(address);
            logger.info("GET /childAlert -> 200 OK, children={}", children.size());
            logger.debug("Children detail sample: {}", children.stream().limit(2).toList());
            return children;
        } catch (Exception e) {
            logger.error("GET /childAlert FAILED for address {}: {}", address, e.getMessage(), e);
            throw e;
        }
    }

    @GetMapping("/phoneAlert")
    public List<String> getPhoneByFireStation(@RequestParam("firestation") String fireStationNumber) {
        logger.info("GET /phoneAlert?firestation={}", fireStationNumber);
        try {
            List<String> phones = personService.getPhoneByFireStation(fireStationNumber);
            logger.info("GET /phoneAlert -> 200 OK, phones={}", phones.size());
            logger.debug("Sample phones: {}", phones.stream().limit(3).toList());
            return phones;
        } catch (Exception e) {
            logger.error("GET /phoneAlert FAILED for station {}: {}", fireStationNumber, e.getMessage(), e);
            throw e;
        }
    }

    @GetMapping("/fire")
    public FireAlertAddressDTO getFireAlert(@RequestParam("address") String address) {
        logger.info("GET /fire?address={}", address);
        try {
            FireAlertAddressDTO dto = personService.getFireAlert(address);
            if (dto == null) {
                logger.info("GET /fire -> 200 OK, result=empty (address={})", address);
            } else {
                logger.info("GET /fire -> 200 OK, residents={}", dto.getResidents().size());
                logger.debug("Resident sample: {}", dto.getResidents().stream().limit(2).toList());
            }
            return dto;
        } catch (Exception e) {
            logger.error("GET /fire FAILED for address {}: {}", address, e.getMessage(), e);
            throw e;
        }
    }

    @GetMapping("/flood/stations")
    public FloodAlertDTO getFloodAlert(@RequestParam("stations") List<String> stations) {
        logger.info("GET /flood/stations?stations={}", stations);
        try {
            FloodAlertDTO dto = personService.getFloodAlert(stations);
            logger.info("GET /flood/stations -> 200 OK, households={}", dto.getAddressList().size());
            logger.debug("First households: {}", dto.getAddressList().stream().limit(2).toList());
            return dto;
        } catch (Exception e) {
            logger.error("GET /flood/stations FAILED for stations {}: {}", stations, e.getMessage(), e);
            throw e;
        }
    }

    @GetMapping("/personInfolastName")
    public List<PersonInfoDTO> getPersonInfo(@RequestParam("lastName") String lastName) {
        logger.info("GET /personInfolastName?lastName={}", lastName);
        try {
            List<PersonInfoDTO> info = personService.getPersonInfo(lastName);
            logger.info("GET /personInfolastName -> 200 OK, results={}", info.size());
            logger.debug("Sample info: {}", info.stream().limit(2).toList());
            return info;
        } catch (Exception e) {
            logger.error("GET /personInfolastName FAILED for lastName {}: {}", lastName, e.getMessage(), e);
            throw e;
        }
    }

    @GetMapping("/communityEmail")
    public List<String> getEmail(@RequestParam("city") String city) {
        logger.info("GET /communityEmail?city={}", city);
        try {
            List<String> emails = personService.getEmail(city);
            logger.info("GET /communityEmail -> 200 OK, emails={}", emails.size());
            logger.debug("Sample emails: {}", emails.stream().limit(3).toList());
            return emails;
        } catch (Exception e) {
            logger.error("GET /communityEmail FAILED for city {}: {}", city, e.getMessage(), e);
            throw e;
        }
    }
}
