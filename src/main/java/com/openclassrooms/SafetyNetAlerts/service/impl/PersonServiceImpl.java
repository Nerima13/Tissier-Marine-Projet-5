package com.openclassrooms.SafetyNetAlerts.service.impl;

import com.openclassrooms.SafetyNetAlerts.dto.DataDTO;
import com.openclassrooms.SafetyNetAlerts.dto.request.childAlert.ChildDTO;
import com.openclassrooms.SafetyNetAlerts.dto.request.childAlert.FamilyMemberDTO;
import com.openclassrooms.SafetyNetAlerts.dto.request.fireAlert.FireAlertAddressDTO;
import com.openclassrooms.SafetyNetAlerts.dto.request.fireAlert.FireAlertPersonDTO;
import com.openclassrooms.SafetyNetAlerts.dto.request.firestationNumber.FireStationCoverageDTO;
import com.openclassrooms.SafetyNetAlerts.dto.request.firestationNumber.PersonByStationNumberDTO;
import com.openclassrooms.SafetyNetAlerts.dto.request.floodAlert.FloodAlertAddressDTO;
import com.openclassrooms.SafetyNetAlerts.dto.request.floodAlert.FloodAlertDTO;
import com.openclassrooms.SafetyNetAlerts.dto.request.floodAlert.FloodAlertPersonDTO;
import com.openclassrooms.SafetyNetAlerts.dto.request.personInfo.PersonInfoDTO;
import com.openclassrooms.SafetyNetAlerts.model.FireStation;
import com.openclassrooms.SafetyNetAlerts.model.MedicalRecord;
import com.openclassrooms.SafetyNetAlerts.model.Person;
import com.openclassrooms.SafetyNetAlerts.repository.PersonRepository;
import com.openclassrooms.SafetyNetAlerts.service.FireStationService;
import com.openclassrooms.SafetyNetAlerts.service.MedicalRecordService;
import com.openclassrooms.SafetyNetAlerts.service.PersonService;
import com.openclassrooms.SafetyNetAlerts.writer.IJsonWriter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class PersonServiceImpl implements PersonService {

    private static final Logger logger = LogManager.getLogger(PersonServiceImpl.class);

    private static final int UNKNOWN_AGE = -1;

    @Autowired
    FireStationService fireStationService;
    @Autowired
    MedicalRecordService medicalRecordService;

    @Autowired
    @Qualifier("personRepoSingleton")
    private PersonRepository personRepository;

    @Autowired
    private IJsonWriter jsonWriter;

    @Override
    public void add(Person person) {
        logger.info("Add person {} {}", person.getFirstName(), person.getLastName());
        logger.debug("Add payload: {}", person);
        try {
            personRepository.add(person);
            logger.info("Person added successfully: {} {}", person.getFirstName(), person.getLastName());

            DataDTO dto = new DataDTO(
                    personRepository.findAll(),
                    fireStationService.findAll(),
                    medicalRecordService.findAll());
            jsonWriter.writeJsonFile(dto);

        } catch (IOException e) {
            logger.error("Failed to write data.json after add: {}", e.getMessage(), e);
            throw new RuntimeException("Unable to write data.json", e);
        } catch (Exception e) {
            logger.error("Add person FAILED for {} {}: {}", person.getFirstName(), person.getLastName(), e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public void delete(Person person) {
        logger.info("Delete person {} {}", person.getFirstName(), person.getLastName());
        try {
            personRepository.delete(person);
            logger.info("Person deleted successfully: {} {}", person.getFirstName(), person.getLastName());
            DataDTO dto = new DataDTO(
                    personRepository.findAll(),
                    fireStationService.findAll(),
                    medicalRecordService.findAll());
            jsonWriter.writeJsonFile(dto);

        } catch (IOException e) {
            logger.error("Failed to write data.json after delete: {}", e.getMessage(), e);
            throw new RuntimeException("Unable to write data.json", e);
        } catch (Exception e) {
            logger.error("Delete person FAILED for {} {}: {}", person.getFirstName(), person.getLastName(), e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public void update(Person person) {
        logger.info("Update person {} {}", person.getFirstName(), person.getLastName());
        logger.debug("Update payload: {}", person);
        try {
            personRepository.update(person);
            logger.info("Person updated successfully: {} {}", person.getFirstName(), person.getLastName());

            DataDTO dto = new DataDTO(
                    personRepository.findAll(),
                    fireStationService.findAll(),
                    medicalRecordService.findAll());
            jsonWriter.writeJsonFile(dto);

        } catch (IOException e) {
            logger.error("Failed to write data.json after update: {}", e.getMessage(), e);
            throw new RuntimeException("Unable to write data.json", e);
        } catch (Exception e) {
            logger.error("Update person FAILED for {} {}: {}", person.getFirstName(), person.getLastName(), e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public Person get(Person person) {
        logger.debug("Get person {} {}", person.getFirstName(), person.getLastName());
        try {
            Person result = personRepository.get(person);
            if (result == null) {
                logger.info("Get person -> result=empty ({} {})", person.getFirstName(), person.getLastName());
            } else {
                logger.info("Get person -> found ({} {})", person.getFirstName(), person.getLastName());
                logger.debug("Get person -> {}", result);
            }
            return result;
        } catch (Exception e) {
            logger.error("Get person FAILED for {} {}: {}", person.getFirstName(), person.getLastName(), e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public List<Person> findAll() {
        logger.info("FindAll persons");
        try {
            List<Person> list = personRepository.findAll();
            logger.info("FindAll -> count={}", list.size());
            logger.debug("FindAll sample: {}", list.stream().limit(3).toList());
            return list;
        } catch (Exception e) {
            logger.error("findAll persons FAILED: {}", e.getMessage(), e);
            throw e;
        }
    }

    public int getAge(Person person) {
        logger.debug("Compute age for {} {}", person.getFirstName(), person.getLastName());
        try {
            MedicalRecord medicalRecord = new MedicalRecord(person.getFirstName(), person.getLastName());
            MedicalRecord targetRecord = medicalRecordService.get(medicalRecord);
            if (targetRecord == null) {
                logger.error("Could not find age for {} {}", person.getFirstName(), person.getLastName());
                return UNKNOWN_AGE;
            }
            String birthdateAsString = targetRecord.getBirthdate();
            LocalDate birthdate = LocalDate.parse(birthdateAsString, DateTimeFormatter.ofPattern("MM/dd/yyyy"));
            int years = Period.between(birthdate, LocalDate.now()).getYears();
            logger.debug("Computed age for {} {} -> {}", person.getFirstName(), person.getLastName(), years);
            return years;
        } catch (Exception e) {
            logger.error("Compute age FAILED for {} {}: {}", person.getFirstName(), person.getLastName(), e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public FireStationCoverageDTO getPersonsCoveredByStation(String stationNumber) {
        logger.info("Coverage by station stationNumber={}", stationNumber);
        FireStationCoverageDTO result = new FireStationCoverageDTO();

        try {
            if (stationNumber == null) {
                logger.info("Coverage by station -> stationNumber=null -> empty result");
                return result;
            }

            List<PersonByStationNumberDTO> coveredPersons = new ArrayList<>();
            int nbAdult = 0;
            int nbChild = 0;

            // 1) Addresses of firestations covered by the station
            List<String> coveredAddresses = new ArrayList<>();
            List<FireStation> fireStations = fireStationService.findAll();
            logger.debug("Found {} fireStation mappings", fireStations.size());

            for (FireStation f : fireStations) {
                if (f != null && f.getStation() != null && f.getStation().equals(stationNumber)) {
                    coveredAddresses.add(f.getAddress());
                }
            }
            logger.debug("Station {} covers {} addresses", stationNumber, coveredAddresses.size());

            // 2) Browse all people
            List<Person> persons = personRepository.findAll();
            logger.debug("Total persons in repository: {}", persons.size());

            for (Person p : persons) {
                if (p == null || p.getAddress() == null) continue;

                // Check that their addresses are covered
                boolean isCovered = false;
                for (String addr : coveredAddresses) {
                    if (addr != null && addr.equals(p.getAddress())) {
                        isCovered = true;
                        break;
                    }
                }
                if (!isCovered) continue;

                // 3) Build the DTO
                PersonByStationNumberDTO dto = new PersonByStationNumberDTO();
                dto.setFirstName(p.getFirstName());
                dto.setLastName(p.getLastName());
                dto.setAddress(p.getAddress());
                dto.setPhone(p.getPhone());

                // Avoid duplicate people
                boolean alreadyInList = false;
                for (PersonByStationNumberDTO person : coveredPersons) {
                    boolean sameFirstName = p.getFirstName() != null
                            && p.getFirstName().equals(person.getFirstName());
                    boolean sameLastName = p.getLastName() != null
                            && p.getLastName().equals(person.getLastName());
                    boolean sameAddress = p.getAddress() != null
                            && p.getAddress().equals(person.getAddress());

                    if (sameFirstName && sameLastName && sameAddress) {
                        alreadyInList = true;
                        break;
                    }
                }
                if (!alreadyInList) {
                    coveredPersons.add(dto);

                    // 4) Count children (≤18) and adults
                    int age = getAge(p);
                    if (age == UNKNOWN_AGE) {
                        continue;
                    }
                    if (age <= 18) {
                        nbChild++;
                    } else {
                        nbAdult++;
                    }
                }
            }

            result.setStation(stationNumber);
            result.setInfoPerson(coveredPersons);
            result.setNbAdult(nbAdult);
            result.setNbChild(nbChild);

            logger.info("Coverage station={} -> residents={}, adults={}, children={}",
                    stationNumber, coveredPersons.size(), nbAdult, nbChild);
            logger.debug("Coverage sample phones: {}",
                    coveredPersons.stream().map(PersonByStationNumberDTO::getPhone).limit(3).toList());
            return result;

        } catch (Exception e) {
            logger.error("Coverage by station FAILED for station {}: {}", stationNumber, e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public List<ChildDTO> getChildInfos(String address) {
        logger.info("ChildAlert for address='{}'", address);
        List<ChildDTO> result = new ArrayList<>();

        try {
            if (address == null) {
                logger.info("childAlert -> address=null -> empty result");
                return result;
            }

            // 1) List the people living at this address
            List<Person> personsAtAddress = new ArrayList<>();
            List<Person> persons = personRepository.findAll();
            logger.debug("Persons total: {}", persons.size());

            for (int i = 0; i < persons.size(); i++) {
                Person p = persons.get(i);
                if (p != null && p.getAddress() != null && p.getAddress().equals(address)) {
                    personsAtAddress.add(p);
                }
            }
            logger.debug("Persons at address='{}': {}", address, personsAtAddress.size());
            if (personsAtAddress.isEmpty()) {
                logger.info("childAlert -> 0 resident at address '{}'", address);
                return result;
            }

            // 2) For each person at the address, if a child (≤ 18), construct the DTO
            for (int i = 0; i < personsAtAddress.size(); i++) {
                Person child = personsAtAddress.get(i);

                int age = getAge(child);

                if (age != UNKNOWN_AGE && age <= 18) {
                    ChildDTO dto = new ChildDTO();
                    dto.setFirstName(child.getFirstName());
                    dto.setLastName(child.getLastName());
                    dto.setAge(age);

                    // 3) List of other household members
                    List<FamilyMemberDTO> family = new ArrayList<>();

                    for (int f = 0; f < personsAtAddress.size(); f++) {
                        Person familyMember = personsAtAddress.get(f);
                        if (familyMember != null) {
                            boolean sameFirst = familyMember.getFirstName() != null
                                    && familyMember.getFirstName().equals(child.getFirstName());
                            boolean sameLast = familyMember.getLastName() != null
                                    && familyMember.getLastName().equals(child.getLastName());

                            // Exclude the child himself
                            if (!(sameFirst && sameLast)) {
                                FamilyMemberDTO member = new FamilyMemberDTO();
                                member.setFirstName(familyMember.getFirstName());
                                member.setLastName(familyMember.getLastName());

                                family.add(member);
                            }
                        }
                    }
                    dto.setFamily(family);
                    result.add(dto);
                }
            }
            logger.info("childAlert address='{}' -> children={}", address, result.size());
            logger.debug("childAlert sample: {}", result.stream().limit(2).toList());
            return result;

        } catch (Exception e) {
            logger.error("childAlert FAILED for address {}: {}", address, e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public List<String> getPhoneByFireStation(String fireStationNumber) {
        logger.info("PhoneAlert for station={}", fireStationNumber);
        Set<String> result = new HashSet<>();

        try {
            if (fireStationNumber == null) {
                logger.info("phoneAlert -> station=null -> empty result");
                return new ArrayList<>();
            }

            // 1) Retrieve the addresses covered by the requested station
            List<String> addresses = new ArrayList<>();
            List<FireStation> fireStations = fireStationService.findAll();
            logger.debug("Mappings total: {}", fireStations.size());

            for (int f = 0; f < fireStations.size(); f++) {
                FireStation fs = fireStations.get(f);
                if (fs != null && fs.getStation() != null && fs.getAddress() != null) {
                    if (fs.getStation().equals(fireStationNumber)) {
                        addresses.add(fs.getAddress());
                    }
                }
            }

            // If no address is covered, we return []
            if (addresses.isEmpty()) {
                logger.info("phoneAlert station={} -> 0 covered address", fireStationNumber);
                return new ArrayList<>();
            }
            logger.debug("phoneAlert station={} covers {} addresses", fireStationNumber, addresses.size());

            // 2) List the people living at these addresses
            List<Person> coveredPersons = new ArrayList<>();
            List<Person> persons = personRepository.findAll();
            logger.debug("Persons total: {}", persons.size());

            for (int p = 0; p < persons.size(); p++) {
                Person person = persons.get(p);
                if (person != null && person.getAddress() != null && person.getPhone() != null) {

                    boolean isCovered = false;
                    for (int a = 0; a < addresses.size(); a++) {
                        if (person.getAddress().equals(addresses.get(a))) {
                            isCovered = true;
                            break;
                        }
                    }
                    if (isCovered) {
                        coveredPersons.add(person);
                    }
                }
            }

            // 3) Extract phone numbers
            for (int i = 0; i < coveredPersons.size(); i++) {
                String phone = coveredPersons.get(i).getPhone();
                if (phone != null) {
                    result.add(phone);
                }
            }

            logger.info("phoneAlert station={} -> phones={}", fireStationNumber, result.size());
            logger.debug("phoneAlert sample: {}", result.stream().limit(3).toList());
            return new ArrayList<>(result);

        } catch (Exception e) {
            logger.error("phoneAlert FAILED for station {}: {}", fireStationNumber, e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public FireAlertAddressDTO getFireAlert(String address) {
        logger.info("FireAlert for address='{}'", address);
        FireAlertAddressDTO result = new FireAlertAddressDTO();

        try {
            if (address == null) {
                logger.info("fireAlert -> address=null -> empty result");
                return result;
            }

            // 1) Find the station number that serves the address
            String stationNumber = null;
            List<FireStation> fireStations = fireStationService.findAll();
            logger.debug("Found {} fireStation mappings", fireStations.size());

            for (int f = 0; f < fireStations.size(); f++) {
                FireStation fs = fireStations.get(f);
                if (fs != null && fs.getAddress() != null && fs.getAddress().equals(address)) {
                    stationNumber = fs.getStation();
                    break;
                }
            }
            result.setStation(stationNumber);

            // 2) List the people living at this address
            List<Person> persons = personRepository.findAll();
            logger.debug("Persons total: {}", persons.size());
            List<FireAlertPersonDTO> residents = new ArrayList<FireAlertPersonDTO>();

            for (int i = 0; i < persons.size(); i++) {
                Person p = persons.get(i);
                if (p != null && p.getAddress() != null && p.getAddress().equals(address)) {

                    // 3) Build the DTO for each person
                    FireAlertPersonDTO dto = new FireAlertPersonDTO();
                    dto.setFirstName(p.getFirstName());
                    dto.setLastName(p.getLastName());
                    dto.setPhone(p.getPhone());

                    MedicalRecord mr = medicalRecordService.get(new MedicalRecord(p.getFirstName(), p.getLastName()));

                    int age = 0;
                    if (mr != null && mr.getBirthdate() != null) {
                        try {
                            LocalDate dob = LocalDate.parse(mr.getBirthdate(), DateTimeFormatter.ofPattern("MM/dd/yyyy"));
                            age = Period.between(dob, LocalDate.now()).getYears();
                        } catch (Exception ignored) {
                        }
                    }
                    dto.setAge(age);

                    List<String> meds = new ArrayList<>();
                    if (mr != null && mr.getMedications() != null) meds.addAll(mr.getMedications());
                    dto.setMedications(meds);

                    List<String> allergies = new ArrayList<>();
                    if (mr != null && mr.getAllergies() != null) allergies.addAll(mr.getAllergies());
                    dto.setAllergies(allergies);

                    residents.add(dto);
                }
            }
            result.setResidents(residents);

            logger.info("fireAlert address='{}' -> residents={}", address, residents.size());
            logger.debug("fireAlert sample residents (names only): {}",
                    residents.stream().limit(2).map(r -> r.getFirstName() + " " + r.getLastName()).toList());
            logger.debug("fireAlert medical details loaded (omitted in logs)");
            return result;

        } catch (Exception e) {
            logger.error("fireAlert FAILED for address {}: {}", address, e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public FloodAlertDTO getFloodAlert(List<String> stations) {
        logger.info("FloodAlert for stations={}", stations);
        FloodAlertDTO result = new FloodAlertDTO();

        try {
            if (stations == null) {
                logger.info("floodAlert -> stations=null -> empty result");
                return result;
            }

            result.setStations(stations);

            List<FireStation> fireStations = fireStationService.findAll();
            List<Person> persons = personRepository.findAll();
            logger.debug("Found {} fireStation mappings", fireStations.size());
            logger.debug("Persons total: {}", persons.size());

            // 1) Retrieve the addresses covered by each station
            List<String> coveredAddresses = new ArrayList<>();

            for (int s = 0; s < stations.size(); s++) {
                String stationNumber = stations.get(s);

                for (int f = 0; f < fireStations.size(); f++) {
                    FireStation fs = fireStations.get(f);
                    if (fs != null && fs.getStation() != null && fs.getAddress() != null
                            && fs.getStation().equals(stationNumber)) {
                        boolean exists = false;
                        for (int a = 0; a < coveredAddresses.size(); a++) {
                            if (coveredAddresses.get(a).equals(fs.getAddress())) {
                                exists = true;
                                break;
                            }
                        }
                        if (!exists) {
                            coveredAddresses.add(fs.getAddress());
                        }
                    }
                }
            }
            logger.debug("floodAlert -> stations={} cover {} addresses", stations, coveredAddresses.size());

            // 2) For each address covered, add the residents + requested information
            for (int a = 0; a < coveredAddresses.size(); a++) {
                String address = coveredAddresses.get(a);

                FloodAlertAddressDTO addressDTO = new FloodAlertAddressDTO();
                addressDTO.setAddress(address);

                for (int p = 0; p < persons.size(); p++) {
                    Person person = persons.get(p);
                    if (person != null && address.equals(person.getAddress())) {

                        // 3) Build each person's DTO
                        FloodAlertPersonDTO personDTO = new FloodAlertPersonDTO();
                        personDTO.setFirstName(person.getFirstName());
                        personDTO.setLastName(person.getLastName());
                        personDTO.setPhone(person.getPhone());

                        MedicalRecord mr = medicalRecordService.get(new MedicalRecord(person.getFirstName(), person.getLastName()));

                        int age = 0;
                        if (mr != null && mr.getBirthdate() != null) {
                            try {
                                LocalDate dob = LocalDate.parse(mr.getBirthdate(), DateTimeFormatter.ofPattern("MM/dd/yyyy"));
                                age = Period.between(dob, LocalDate.now()).getYears();
                            } catch (Exception ignored) {
                            }
                        }
                        personDTO.setAge(age);

                        List<String> meds = new ArrayList<>();
                        if (mr != null && mr.getMedications() != null) meds.addAll(mr.getMedications());
                        personDTO.setMedications(meds);

                        List<String> allergies = new ArrayList<>();
                        if (mr != null && mr.getAllergies() != null) allergies.addAll(mr.getAllergies());
                        personDTO.setAllergies(allergies);

                        addressDTO.getListPerson().add(personDTO);
                    }
                }
                result.getAddressList().add(addressDTO);
            }
            logger.info("floodAlert stations={} -> households={}", stations, result.getAddressList().size());
            logger.debug("floodAlert first addresses: {}",
                    result.getAddressList().stream().limit(2).map(FloodAlertAddressDTO::getAddress).toList());
            logger.debug("floodAlert medical details loaded (omitted in logs)");
            return result;

        } catch (Exception e) {
            logger.error("floodAlert FAILED for stations {}: {}", stations, e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public List<PersonInfoDTO> getPersonInfo(String lastName) {
        logger.info("PersonInfo lastName='{}'", lastName);
        List<PersonInfoDTO> result = new ArrayList<>();

        try {
            if (lastName == null) {
                logger.info("personInfo -> lastName=null -> empty result");
                return result;
            }

            // 1) List all people and their medical records
            List<Person> persons = personRepository.findAll();
            List<MedicalRecord> medicalRecords = medicalRecordService.findAll();
            logger.debug("personInfo repository sizes: persons={}, medicalRecords={}",
                    persons.size(), medicalRecords.size());

            // Pick out those with the same lastName
            for (Person person : persons) {
                if (person.getLastName().equals(lastName)) {

                    // 2) Search for the corresponding medical record
                    MedicalRecord personMedicalRecord = null;
                    for (MedicalRecord medicalRecord : medicalRecords) {
                        boolean sameFirstName = medicalRecord.getFirstName().equals(person.getFirstName());
                        boolean sameLastName = medicalRecord.getLastName().equals(person.getLastName());

                        if (sameFirstName && sameLastName) {
                            personMedicalRecord = medicalRecord;
                            break;
                        }
                    }

                    // 3) Build the DTO with the person's information
                    PersonInfoDTO dto = new PersonInfoDTO();
                    dto.setFirstName(person.getFirstName());
                    dto.setLastName(person.getLastName());
                    dto.setAddress(person.getAddress());
                    int age = getAge(person);
                    if (age != UNKNOWN_AGE) {
                        dto.setAge(age);
                    }
                    dto.setEmail(person.getEmail());

                    if (personMedicalRecord != null) {
                        dto.setMedications(personMedicalRecord.getMedications());
                        dto.setAllergies(personMedicalRecord.getAllergies());
                    } else {
                        dto.setMedications(new ArrayList<>());
                        dto.setAllergies(new ArrayList<>());
                    }
                    result.add(dto);
                }
            }
            logger.info("personInfo lastName='{}' -> results={}", lastName, result.size());
            logger.debug("personInfo sample names: {}",
                    result.stream().limit(2).map(x -> x.getFirstName() + " " + x.getLastName()).toList());
            logger.debug("personInfo medical details loaded (omitted in logs)");
            return result;

        } catch (Exception e) {
            logger.error("personInfo FAILED for lastName {}: {}", lastName, e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public List<String> getEmail(String city) {
        logger.info("CommunityEmail city='{}'", city);
        Set<String> result = new HashSet<>();

        try {
            if (city == null) {
                logger.info("communityEmail -> city=null -> empty result");
                return new ArrayList<>();
            }

            // 1) List all people
            List<Person> persons = personRepository.findAll();
            logger.debug("communityEmail persons total: {}", persons.size());

            // 2) Get the email of the residents
            for (Person person : persons) {
                if (person.getCity().equals(city)) {
                    result.add(person.getEmail());
                }
            }

            logger.info("communityEmail city='{}' -> emails={}", city, result.size());
            logger.debug("communityEmail emails loaded (omitted in logs)");
            return new ArrayList<>(result);

        } catch (Exception e) {
            logger.error("communityEmail FAILED for city {}: {}", city, e.getMessage(), e);
            throw e;
        }
    }
}

