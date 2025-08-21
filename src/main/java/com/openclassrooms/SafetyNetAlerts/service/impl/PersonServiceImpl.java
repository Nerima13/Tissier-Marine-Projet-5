package com.openclassrooms.SafetyNetAlerts.service.impl;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

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
import com.openclassrooms.SafetyNetAlerts.service.FireStationService;
import com.openclassrooms.SafetyNetAlerts.service.MedicalRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.openclassrooms.SafetyNetAlerts.model.Person;
import com.openclassrooms.SafetyNetAlerts.repository.PersonRepository;
import com.openclassrooms.SafetyNetAlerts.service.PersonService;

@Service
public class PersonServiceImpl implements PersonService {

@Autowired
    FireStationService fireStationService;
@Autowired
    MedicalRecordService medicalRecordService;

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

    public int getAge(Person person) {
        MedicalRecord medicalRecord = new MedicalRecord(person.getFirstName(), person.getLastName());
        String birthdateAsString = medicalRecordService.get(medicalRecord).getBirthdate();
        LocalDate birthdate = LocalDate.parse(birthdateAsString, DateTimeFormatter.ofPattern("MM/dd/yyyy"));

        return Period.between(birthdate, LocalDate.now()).getYears();
    }

    @Override
    public FireStationCoverageDTO getPersonsCoveredByStation(String stationNumber) {
        FireStationCoverageDTO result = new FireStationCoverageDTO();

        if (stationNumber == null) {
            return result;
        }

        List<PersonByStationNumberDTO> coveredPersons = new ArrayList<>();
        int nbAdult = 0;
        int nbChild = 0;

        // 1) Addresses of firestations covered by the station
        List<String> coveredAddresses = new ArrayList<>();
        List<FireStation> fireStations = fireStationService.findAll();
        for (FireStation f : fireStations) {
            if (f != null && f.getStation() != null && f.getStation().equals(stationNumber)) {
                coveredAddresses.add(f.getAddress());
            }
        }

        // 2) Browse all people
        List<Person> persons = personRepository.findAll();
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
                boolean sameLastName  = p.getLastName() != null
                        && p.getLastName().equals(person.getLastName());
                boolean sameAddress   = p.getAddress() != null
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
        return result;
    }


    @Override
    public List<ChildDTO> getChildInfos(String address) {
        List<ChildDTO> result = new ArrayList<>();

        if (address == null) {
            return result;
        }

        // 1) List the people living at this address
        List<Person> personsAtAddress = new ArrayList<>();
        List<Person> persons = personRepository.findAll();

        for (int i = 0; i < persons.size(); i++) {
            Person p = persons.get(i);
            if (p != null && p.getAddress() != null && p.getAddress().equals(address)) {
                personsAtAddress.add(p);
            }
        }
        if (personsAtAddress.isEmpty()) {
            return result;
        }

        // 2) For each person at the address, if a child (≤ 18), construct the DTO
        for (int i = 0; i < personsAtAddress.size(); i++) {
            Person child = personsAtAddress.get(i);

            int age = getAge(child);

            if (age <= 18) {
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
                        boolean sameLast  = familyMember.getLastName()  != null
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
        return result;
    }

    @Override
    public List<String> getPhoneByFireStation(String fireStationNumber) {
        List<String> result = new ArrayList<>();

        if (fireStationNumber == null) {
            return result;
        }

        // 1) Retrieve the addresses covered by the requested station
        List<String> addresses = new ArrayList<>();
        List<FireStation> fireStations = fireStationService.findAll();

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
            return result;
        }

        // 2) List the people living at these addresses
        List<Person> coveredPersons = new ArrayList<>();
        List<Person> persons = personRepository.findAll();

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
        return result;
    }

    @Override
    public FireAlertAddressDTO getFireAlert(String address) {
        FireAlertAddressDTO result = new FireAlertAddressDTO();

        if (address == null) {
            return result;
        }

        // 1) Find the station number that serves the address
        String stationNumber = null;
        List<FireStation> fireStations = fireStationService.findAll();

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
        List<FireAlertPersonDTO> residents = new ArrayList<FireAlertPersonDTO>();

        for (int i = 0; i < persons.size(); i++) {
            Person p = persons.get(i);
            if (p != null && p.getAddress() != null && p.getAddress().equals(address)) {

                // 3) Build the DTO for each person
                FireAlertPersonDTO dto = new FireAlertPersonDTO();
                dto.setFirstName(p.getFirstName());
                dto.setLastName(p.getLastName());
                dto.setPhone(p.getPhone());
                dto.setAge(getAge(p));

                MedicalRecord mr = medicalRecordService.get(new MedicalRecord(p.getFirstName(), p.getLastName()));
                if (mr != null) {
                    if (mr.getMedications() != null) {
                        dto.setMedications(new ArrayList<>(mr.getMedications()));
                    } else {
                        dto.setMedications(new ArrayList<>());
                    }
                    if (mr.getAllergies() != null) {
                        dto.setAllergies(new ArrayList<>(mr.getAllergies()));
                    } else {
                        dto.setAllergies(new ArrayList<>());
                    }
                } else {
                    dto.setMedications(new ArrayList<>());
                    dto.setAllergies(new ArrayList<>());
                }
                residents.add(dto);
            }
        }
        result.setResidents(residents);
        return result;
    }

    @Override
    public FloodAlertDTO getFloodAlert(List<String> stations) {
        FloodAlertDTO result = new FloodAlertDTO();

        if (stations == null) {
            return result;
        }

        List<FireStation> fireStations = fireStationService.findAll();
        List<Person> persons = personRepository.findAll();

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
                    personDTO.setAge(getAge(person));

                    MedicalRecord mr = medicalRecordService.get(new MedicalRecord(person.getFirstName(), person.getLastName()));
                    if (mr != null) {
                        personDTO.setMedications(new ArrayList<>(mr.getMedications()));
                        personDTO.setAllergies(new ArrayList<>(mr.getAllergies()));
                    } else {
                        personDTO.setMedications(new ArrayList<>());
                        personDTO.setAllergies(new ArrayList<>());
                    }

                    addressDTO.getListPerson().add(personDTO);
                    }
                }
            result.setStations(stations);
            result.getAddressList().add(addressDTO);
        }
        return result;
    }

    @Override
    public List<PersonInfoDTO> getPersonInfo(String lastName) {
        List<PersonInfoDTO> result = new ArrayList<>();

        if (lastName == null) {
            return result;
        }

        // 1) List all people and their medical records
        List<Person> persons = personRepository.findAll();
        List<MedicalRecord> medicalRecords = medicalRecordService.findAll();

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
                dto.setAge(getAge(person));
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
        return result;
    }

    @Override
    public List<String> getEmail(String city) {
        List<String> result = new ArrayList<>();

        if (city == null) {
            return result;
        }

        // 1) List all people
        List<Person> persons = personRepository.findAll();

        // 2) Get the email of the residents
        for (Person person : persons) {
            if (person.getCity().equals(city)) {
                result.add(person.getEmail());
            }
        }
        return result;
    }
}
