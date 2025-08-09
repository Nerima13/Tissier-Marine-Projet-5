package com.openclassrooms.SafetyNetAlerts.service.impl;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import com.openclassrooms.SafetyNetAlerts.dto.request.*;
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

    public int getChildCount(Person person) {
        int nbChild = 0;
        int age = getAge(person);

        if (age < 18) nbChild++;
        return nbChild;
    }

    @Override
    public FireStationCoverageDTO getPersonsCoveredByStation(String station) {
        List<FireStation> fireStations = fireStationService.findAll();

        for(int i = 0; i< fireStations.size(); i++) {
            String fireStationAddress = fireStations.get(i).getAddress();

            List<Person> persons = findAll();
            for (int y = 0; y < persons.size(); y++) {
                String personAddress = persons.get(y).getAddress();
                if (fireStationAddress.equals(personAddress)) {

                    getChildCount(persons.get(y));
                    System.err.println(getChildCount(persons.get(y)));
                }
                return null;
            }

        }
        return null;
        // TODO : renvoyer la liste des adresses des habitants couverts par leur station correspondante
        // TODO : match adresses personnes et adresse firestations

        /**
         *
         */
        // TODO : utiliser le PersonByStationNumberDTO qui renseigne le nom/adresse/phone des habitants
        // TODO : décompte des adultes et des enfants
    }

    @Override
    public ChildDTO getChildInfos(String address) {
        return null;
    }

    @Override
    public List<String> getPhoneByFireStation(String fireStationNumber) {
        return List.of();
    }

    @Override
    public FireAlertDTO getFireAlert(String address) {
        return null;
    }

    @Override
    public FloodAlertDTO getFloodAlert(List<String> stations) {
        return null;
    }

    @Override
    public List<PersonInfoDTO> getPersonInfo(String lastName) {
        List<PersonInfoDTO> personInfoList = new ArrayList<>();
        List<Person> persons = personRepository.findAll();
        List<MedicalRecord> medicalRecords = medicalRecordService.findAll();

        for (Person person : persons) {
            if (person.getLastName().equals(lastName)) {

                MedicalRecord personMedicalRecord = null;
                for (MedicalRecord medicalRecord : medicalRecords) {
                    boolean sameFirstName = medicalRecord.getFirstName().equals(person.getFirstName());
                    boolean sameLastName = medicalRecord.getLastName().equals(person.getLastName());

                    if (sameFirstName && sameLastName) {
                        personMedicalRecord = medicalRecord;
                        break;
                    }
                }

                PersonInfoDTO dto = new PersonInfoDTO();
                dto.setFirstName(person.getFirstName());
                dto.setLastName(person.getLastName());
                dto.setAddress(person.getAddress());
                dto.setAge(getAge(person));

                if (personMedicalRecord != null) {
                    dto.setMedications(personMedicalRecord.getMedications());
                    dto.setAllergies(personMedicalRecord.getAllergies());
                } else {
                    dto.setMedications(new ArrayList<>());
                    dto.setAllergies(new ArrayList<>());
                }
                personInfoList.add(dto);
            }
        }
        return personInfoList;
    }

    @Override
    public List<String> getEmail(String city) {
        List<String> emails = new ArrayList<>();
        List<Person> persons = personRepository.findAll();

        for (Person person : persons) {
            if (person.getCity().equals(city)) {
                emails.add(person.getEmail());
            }
        }
        return emails;
    }
}
