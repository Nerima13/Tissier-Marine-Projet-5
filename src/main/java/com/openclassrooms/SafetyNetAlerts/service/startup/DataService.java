package com.openclassrooms.SafetyNetAlerts.service.startup;

import com.openclassrooms.SafetyNetAlerts.dto.DataDTO;
import com.openclassrooms.SafetyNetAlerts.reader.IJsonReader;
import com.openclassrooms.SafetyNetAlerts.reader.ReadDataFromJson;
import com.openclassrooms.SafetyNetAlerts.repository.FireStationRepository;
import com.openclassrooms.SafetyNetAlerts.repository.MedicalRecordRepository;
import com.openclassrooms.SafetyNetAlerts.repository.PersonRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class DataService implements CommandLineRunner {

    private static final Logger logger = LogManager.getLogger(DataService.class);

    @Autowired
    @Qualifier("personRepoSingleton")
    private PersonRepository personRepository;

    @Autowired
    @Qualifier("fireStationRepoSingleton")
    private FireStationRepository fireStationRepository;

    @Autowired
    @Qualifier("medicalRecordRepoSingleton")
    private MedicalRecordRepository medicalRecordRepository;

    @Override
    public void run(String... args) {
        try {

            IJsonReader readJsonData = new ReadDataFromJson();
            DataDTO dto = readJsonData.readJsonFile();
            personRepository.addAll(dto.getListPersons());
            medicalRecordRepository.addAll(dto.getListMedicalRecords());
            fireStationRepository.addAll(dto.getListFireStations());

        } catch (IOException e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }
    }
}