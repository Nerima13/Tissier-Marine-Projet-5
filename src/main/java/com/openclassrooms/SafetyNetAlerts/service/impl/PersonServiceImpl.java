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

    @Override
    public FireStationCoverageDTO getPersonsCoveredByStation(String stationNumber) {
        FireStationCoverageDTO result = new FireStationCoverageDTO();
        result.setStation(stationNumber);

        List<PersonByStationNumberDTO> coveredPersons = new ArrayList<>();
        int nbAdult = 0;
        int nbChild = 0;

        // 1) Adresses de casernes couvertes par la station
        List<String> coveredAddresses = new ArrayList<>();
        List<FireStation> fireStations = fireStationService.findAll();
        for (FireStation f : fireStations) {
            if (f != null && f.getStation() != null && f.getStation().equals(stationNumber)) {
                coveredAddresses.add(f.getAddress());
            }
        }

        // 2) Parcourir toutes les personnes
        List<Person> persons = personRepository.findAll();
        for (Person p : persons) {
            if (p == null || p.getAddress() == null) continue;

            // Vérifier que leurs adresses sont couvertes
            boolean isCovered = false;
            for (String addr : coveredAddresses) {
                if (addr != null && addr.equals(p.getAddress())) {
                    isCovered = true;
                    break;
                }
            }
            if (!isCovered) continue;

            // 3) Construire le dto
            PersonByStationNumberDTO dto = new PersonByStationNumberDTO();
            dto.setFirstName(p.getFirstName());
            dto.setLastName(p.getLastName());
            dto.setAddress(p.getAddress());
            dto.setPhone(p.getPhone());

            // Éviter les doublons de personnes
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

                // 4) Compter enfants (≤18) et adultes
                int age = getAge(p);
                if (age <= 18) {
                    nbChild++;
                } else {
                    nbAdult++;
                }
            }
        }

        result.setInfoPerson(coveredPersons);
        result.setNbAdult(nbAdult);
        result.setNbChild(nbChild);
        return result;
    }


    @Override
    public ChildDTO getChildInfos(String address) {
        return null;
    }

    @Override
    public List<String> getPhoneByFireStation(String fireStationNumber) {
        List<String> result = new ArrayList<>();

        if (fireStationNumber == null) {
            return result;
        }

        // 1) Récupérer les adresses couvertes par la station demandée
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

        // Si aucune adresse couverte, on renvoie []
        if (addresses.isEmpty()) {
            return result;
        }

        // 2) Lister les personnes vivant à ces adresses
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

        // 3) Extraire les numéros de téléphone
        for (int i = 0; i < coveredPersons.size(); i++) {
            String phone = coveredPersons.get(i).getPhone();
            if (phone != null) {
                result.add(phone);
            }
        }
        return result;
    }

    @Override
    public FireAlertDTO getFireAlert(String address) {
        return null;
    }

    @Override
    public FloodAlertDTO getFloodAlert(List<String> stations) {
        FloodAlertDTO result = new FloodAlertDTO();

        List<FireStation> fireStations = fireStationService.findAll();
        List<Person> persons = personRepository.findAll();
        List<MedicalRecord> medicalRecords = medicalRecordService.findAll();

        return result;
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
