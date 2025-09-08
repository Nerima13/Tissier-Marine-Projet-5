package com.openclassrooms.SafetyNetAlerts.service.impl;

import java.io.IOException;
import java.util.List;

import com.openclassrooms.SafetyNetAlerts.dto.DataDTO;
import com.openclassrooms.SafetyNetAlerts.repository.MedicalRecordRepository;
import com.openclassrooms.SafetyNetAlerts.repository.PersonRepository;
import com.openclassrooms.SafetyNetAlerts.writer.IJsonWriter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.openclassrooms.SafetyNetAlerts.model.FireStation;
import com.openclassrooms.SafetyNetAlerts.repository.FireStationRepository;
import com.openclassrooms.SafetyNetAlerts.service.FireStationService;

@Service
public class FireStationServiceImpl implements FireStationService {

    private static Logger logger = LogManager.getLogger(FireStationServiceImpl.class);

    @Autowired
    @Qualifier("fireStationRepoSingleton")
    private FireStationRepository fireStationRepository;

    @Autowired
    @Qualifier("personRepoSingleton")
    private PersonRepository personRepository;

    @Autowired
    @Qualifier("medicalRecordRepoSingleton")
    private MedicalRecordRepository medicalRecordRepository;

    @Autowired
    private IJsonWriter jsonWriter;
	
	@Override
	public void add(FireStation fireStation) {
        logger.info("Add fireStation mapping {} -> station {}",
                fireStation.getAddress(), fireStation.getStation());
        logger.debug("Add payload: {}", fireStation);
        try {
            fireStationRepository.add(fireStation);
            logger.info("FireStation mapping added successfully: {} -> {}",
                    fireStation.getAddress(), fireStation.getStation());

            DataDTO dto = new DataDTO(
                    personRepository.findAll(),
                    fireStationRepository.findAll(),
                    medicalRecordRepository.findAll());
            jsonWriter.writeJsonFile(dto);

        } catch (IOException e) {
            logger.error("Failed to write data.json after add: {}", e.getMessage(), e);
            throw new RuntimeException("Unable to write data.json", e);
        } catch (Exception e) {
            logger.error("Add fireStation FAILED for mapping {} -> {}: {}", fireStation.getAddress(), fireStation.getStation(), e.getMessage(), e);
            throw e;
        }
    }

	@Override
	public void delete(FireStation fireStation) {
        logger.info("Delete fireStation mapping {} -> station {}",
                fireStation.getAddress(), fireStation.getStation());
        try {
            fireStationRepository.delete(fireStation);
            logger.info("FireStation mapping deleted successfully: {} -> {}",
                    fireStation.getAddress(), fireStation.getStation());

            DataDTO dto = new DataDTO(
                    personRepository.findAll(),
                    fireStationRepository.findAll(),
                    medicalRecordRepository.findAll());
            jsonWriter.writeJsonFile(dto);

        } catch (IOException e) {
            logger.error("Failed to write data.json after delete: {}", e.getMessage(), e);
            throw new RuntimeException("Unable to write data.json", e);
        } catch (Exception e) {
            logger.error("Delete fireStation FAILED for mapping {} -> {}: {}", fireStation.getAddress(), fireStation.getStation(), e.getMessage(), e);
            throw e;
        }
    }

	@Override
	public void update(FireStation fireStation) {
        logger.info("Update fireStation mapping {} -> station {}",
                fireStation.getAddress(), fireStation.getStation());
        logger.debug("Update payload: {}", fireStation);
        try {
            fireStationRepository.update(fireStation);
            logger.info("FireStation mapping updated successfully: {} -> {}",
                    fireStation.getAddress(), fireStation.getStation());

            DataDTO dto = new DataDTO(
                    personRepository.findAll(),
                    fireStationRepository.findAll(),
                    medicalRecordRepository.findAll());
            jsonWriter.writeJsonFile(dto);

        } catch (IOException e) {
            logger.error("Failed to write data.json after update: {}", e.getMessage(), e);
            throw new RuntimeException("Unable to write data.json", e);
        } catch (Exception e) {
            logger.error("Update fireStation FAILED for mapping {} -> {}: {}", fireStation.getAddress(), fireStation.getStation(), e.getMessage(), e);
            throw e;
        }
    }
	
	@Override
    public FireStation get(FireStation fireStation) {
        logger.debug("Get fireStation mapping for address='{}'", fireStation.getAddress());
        try {
            FireStation result = fireStationRepository.get(fireStation);
            if (result == null) {
                logger.info("Get fireStation -> result=empty (address='{}')", fireStation.getAddress());
            } else {
                logger.info("Get fireStation -> found mapping {} -> station {}", result.getAddress(), result.getStation());
                logger.debug("Get result: {}", result);
            }
            return result;
        } catch (Exception e) {
            logger.error("Get fireStation FAILED for address {}: {}", fireStation.getAddress(), e.getMessage(), e);
            throw e;
        }
    }

	@Override
	public List<FireStation> findAll() {
        logger.info("FindAll fireStations");
        try {
            List<FireStation> list = fireStationRepository.findAll();
            logger.info("FindAll fireStations -> count={}", list.size());
            logger.debug("findAll sample: {}", list.stream().limit(3).toList());
            return list;
        } catch (Exception e) {
            logger.error("findAll fireStations FAILED: {}", e.getMessage(), e);
            throw e;
        }
    }
}