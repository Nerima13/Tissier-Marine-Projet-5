package com.openclassrooms.SafetyNetAlerts.service.impl;

import java.io.IOException;
import java.util.List;

import com.openclassrooms.SafetyNetAlerts.dto.DataDTO;
import com.openclassrooms.SafetyNetAlerts.repository.FireStationRepository;
import com.openclassrooms.SafetyNetAlerts.repository.PersonRepository;
import com.openclassrooms.SafetyNetAlerts.writer.IJsonWriter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.openclassrooms.SafetyNetAlerts.model.MedicalRecord;
import com.openclassrooms.SafetyNetAlerts.repository.MedicalRecordRepository;
import com.openclassrooms.SafetyNetAlerts.service.MedicalRecordService;

@Service
public class MedicalRecordServiceImpl implements MedicalRecordService {

    private static Logger logger = LogManager.getLogger(MedicalRecordServiceImpl.class);

    @Autowired
    @Qualifier("personRepoSingleton")
    private PersonRepository personRepository;

    @Autowired
    @Qualifier("fireStationRepoSingleton")
    private FireStationRepository fireStationRepository;

    @Autowired
    private IJsonWriter jsonWriter;

    @Autowired
	@Qualifier("medicalRecordRepoSingleton")
	private MedicalRecordRepository medicalRecordRepository;

	@Override
	public void add(MedicalRecord medicalRecord) {
        logger.info("Add medicalRecord {} {}",
                medicalRecord.getFirstName(), medicalRecord.getLastName());
        logger.debug("Add payload received (medical details omitted)");
        try {
            medicalRecordRepository.add(medicalRecord);
            logger.info("MedicalRecord added successfully: {} {}",
                    medicalRecord.getFirstName(), medicalRecord.getLastName());

            DataDTO dto = new DataDTO(
                    personRepository.findAll(),
                    fireStationRepository.findAll(),
                    medicalRecordRepository.findAll());
            jsonWriter.writeJsonFile(dto);

        } catch (IOException e) {
            logger.error("Failed to write data.json after add: {}", e.getMessage(), e);
            throw new RuntimeException("Unable to write data.json", e);
        } catch (Exception e) {
            logger.error("Add medicalRecord FAILED for {} {}: {}", medicalRecord.getFirstName(), medicalRecord.getLastName(), e.getMessage(), e);
            throw e;
        }
    }

	@Override
	public void delete(MedicalRecord medicalRecord) {
        logger.info("Delete medicalRecord {} {}",
                medicalRecord.getFirstName(), medicalRecord.getLastName());
        try {
            medicalRecordRepository.delete(medicalRecord);
            logger.info("MedicalRecord deleted successfully: {} {}",
                    medicalRecord.getFirstName(), medicalRecord.getLastName());

            DataDTO dto = new DataDTO(
                    personRepository.findAll(),
                    fireStationRepository.findAll(),
                    medicalRecordRepository.findAll());
            jsonWriter.writeJsonFile(dto);

        } catch (IOException e) {
            logger.error("Failed to write data.json after delete: {}", e.getMessage(), e);
            throw new RuntimeException("Unable to write data.json", e);
        } catch (Exception e) {
            logger.error("Delete medicalRecord FAILED for {} {}: {}", medicalRecord.getFirstName(), medicalRecord.getLastName(), e.getMessage(), e);
            throw e;
        }
    }

	@Override
	public void update(MedicalRecord medicalRecord) {
        logger.info("Update medicalRecord {} {}",
                medicalRecord.getFirstName(), medicalRecord.getLastName());
        logger.debug("Update payload received (medical details omitted)");
        try {
            medicalRecordRepository.update(medicalRecord);
            logger.info("MedicalRecord updated successfully: {} {}",
                    medicalRecord.getFirstName(), medicalRecord.getLastName());

            DataDTO dto = new DataDTO(
                    personRepository.findAll(),
                    fireStationRepository.findAll(),
                    medicalRecordRepository.findAll());
            jsonWriter.writeJsonFile(dto);

        } catch (IOException e) {
            logger.error("Failed to write data.json after update: {}", e.getMessage(), e);
            throw new RuntimeException("Unable to write data.json", e);
        } catch (Exception e) {
            logger.error("Update medicalRecord FAILED for {} {}: {}", medicalRecord.getFirstName(), medicalRecord.getLastName(), e.getMessage(), e);
            throw e;
        }
    }
	
	@Override
    public MedicalRecord get(MedicalRecord medicalRecord) {
        logger.debug("Get medicalRecord {} {}",
                medicalRecord.getFirstName(), medicalRecord.getLastName());
        try {
            MedicalRecord result = medicalRecordRepository.get(medicalRecord);
            if (result == null) {
                logger.info("Get medicalRecord -> result=empty ({} {})",
                        medicalRecord.getFirstName(), medicalRecord.getLastName());
            } else {
                logger.info("Get medicalRecord -> found ({} {})",
                        medicalRecord.getFirstName(), medicalRecord.getLastName());
                logger.debug("Medical details loaded (omitted in logs)");
            }
            return result;
        } catch (Exception e) {
            logger.error("Get medicalRecord FAILED for {} {}: {}",
                    medicalRecord.getFirstName(), medicalRecord.getLastName(), e.getMessage(), e);
            throw e;
        }
    }

	@Override
	public List<MedicalRecord> findAll() {
        logger.info("FindAll medicalRecords");
        try {
            List<MedicalRecord> list = medicalRecordRepository.findAll();
            logger.info("FindAll medicalRecords -> count={}", list.size());
            logger.debug("findAll medicalRecords loaded (details omitted)");
            return list;
        } catch (Exception e) {
            logger.error("findAll medicalRecords FAILED: {}", e.getMessage(), e);
            throw e;
        }
    }
}
