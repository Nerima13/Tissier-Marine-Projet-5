package com.openclassrooms.SafetyNetAlerts.repository.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import com.openclassrooms.SafetyNetAlerts.model.MedicalRecord;
import com.openclassrooms.SafetyNetAlerts.repository.MedicalRecordRepository;

@Repository
public class MedicalRecordRepositoryImpl implements MedicalRecordRepository {

    private static final Logger logger = LogManager.getLogger(MedicalRecordRepositoryImpl.class);

	private List<MedicalRecord> medicalRecords = new ArrayList<>();
	
	@Bean
	@Scope("singleton")
	public MedicalRecordRepositoryImpl medicalRecordRepoSingleton() {
		return new MedicalRecordRepositoryImpl();
	}
	
	@Override
	public void add(MedicalRecord medicalRecord) {
        logger.info("Repo add medicalRecord {} {}", medicalRecord.getFirstName(), medicalRecord.getLastName());
        logger.debug("Add payload received (medical details omitted)");
        try {
            medicalRecords.add(medicalRecord);
            logger.info("Repo add -> OK (size={})", medicalRecords.size());
        } catch (Exception e) {
            logger.error("Repo add FAILED for {} {}: {}",
                    medicalRecord.getFirstName(), medicalRecord.getLastName(), e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public void delete(MedicalRecord medicalRecord) {
        logger.info("Repo delete medicalRecord {} {}",
                medicalRecord.getFirstName(), medicalRecord.getLastName());
        try {
            boolean removed = medicalRecords.remove(medicalRecord);
            if (removed) {
                logger.info("Repo delete -> OK (size={})", medicalRecords.size());
            } else {
                logger.error("Repo delete -> medicalRecord not found: {} {}",
                        medicalRecord.getFirstName(), medicalRecord.getLastName());
            }
        } catch (Exception e) {
            logger.error("Repo delete FAILED for {} {}: {}",
                    medicalRecord.getFirstName(), medicalRecord.getLastName(), e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public void update(MedicalRecord medicalRecord) {
        logger.info("Repo update medicalRecord {} {}",
                medicalRecord.getFirstName(), medicalRecord.getLastName());
        logger.debug("Update payload received (medical details omitted)");
        try {
            boolean updated = false;
            for (MedicalRecord m : medicalRecords) {
                if (m.equals(medicalRecord)) {
                    m.setBirthdate(medicalRecord.getBirthdate());
                    m.setMedications(medicalRecord.getMedications());
                    m.setAllergies(medicalRecord.getAllergies());
                    updated = true;
                    break;
                }
            }
            if (updated) {
                logger.info("Repo update -> OK for {} {}",
                        medicalRecord.getFirstName(), medicalRecord.getLastName());
            } else {
                logger.error("Repo update -> medicalRecord not found: {} {}",
                        medicalRecord.getFirstName(), medicalRecord.getLastName());
            }
        } catch (Exception e) {
            logger.error("Repo update FAILED for {} {}: {}",
                    medicalRecord.getFirstName(), medicalRecord.getLastName(), e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public MedicalRecord get(MedicalRecord medicalRecord) {
        logger.debug("Repo get medicalRecord {} {}",
                medicalRecord.getFirstName(), medicalRecord.getLastName());
        try {
            for (MedicalRecord m : medicalRecords) {
                if (m.equals(medicalRecord)) {
                    logger.info("Repo get -> found {} {}",
                            medicalRecord.getFirstName(), medicalRecord.getLastName());
                    logger.debug("Medical record loaded (details omitted)");
                    return m;
                }
            }
            logger.info("Repo get -> result=empty ({} {})",
                    medicalRecord.getFirstName(), medicalRecord.getLastName());
            return null;
        } catch (Exception e) {
            logger.error("Repo get FAILED for {} {}: {}",
                    medicalRecord.getFirstName(), medicalRecord.getLastName(), e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public List<MedicalRecord> findAll() {
        logger.info("Repo findAll medicalRecords");
        try {
            logger.info("Repo findAll -> count={}", medicalRecords.size());
            logger.debug("findAll medicalRecords loaded (details omitted)");
            return medicalRecords;
        } catch (Exception e) {
            logger.error("Repo findAll FAILED: {}", e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public void addAll(List<MedicalRecord> element) {
        try {
            if (element != null) {
                medicalRecords.addAll(element);
            }
            logger.info("Initial data loaded: medicalRecords={}", medicalRecords.size());
        } catch (Exception e) {
            logger.error("Repo add all FAILED: {}", e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public void clear() {
        logger.debug("Repo clear medicalRecords ({} -> 0)", medicalRecords.size());
        medicalRecords.clear();
        logger.info("Repo clear -> OK");
    }
}
