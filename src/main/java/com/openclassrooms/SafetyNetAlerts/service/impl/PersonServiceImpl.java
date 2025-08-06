package com.openclassrooms.SafetyNetAlerts.service.impl;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.Date;
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

    public int getAge(Person person){
        MedicalRecord medicalRecord = new MedicalRecord(person.getFirstName(), person.getLastName());
        String birthdateAsString = medicalRecordService.get(medicalRecord).getBirthdate();
        LocalDate birthdate = LocalDate.parse(birthdateAsString, DateTimeFormatter.ofPattern("MM/dd/yyyy"));

        return Period.between(birthdate, LocalDate.now()).getYears();
    }

    @Override
    public FireStationCoverageDTO getPersonsCoveredByStation(int station) {
        List<FireStation> fireStations = fireStationService.findAll();
        for(int i = 0; i< fireStations.size(); i++) {
            String fireStationAddress = fireStations.get(i).getAddress();

            List<Person> persons = findAll();
            for (int y = 0; y < persons.size(); y++) {
                String personAddress = persons.get(y).getAddress();
                if (fireStationAddress.equals(personAddress)){
                    FireStationCoverageDTO fireStationCoverageDTO = new FireStationCoverageDTO();

                }

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
    public PersonInfoDTO getPersonInfo(String firstName, String lastName) {
        return null;
    }

    @Override
    public List<String> getEmail(String city) {
        return List.of();
    }
}
