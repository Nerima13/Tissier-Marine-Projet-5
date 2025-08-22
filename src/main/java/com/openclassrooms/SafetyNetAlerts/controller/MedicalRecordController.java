package com.openclassrooms.SafetyNetAlerts.controller;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.openclassrooms.SafetyNetAlerts.model.MedicalRecord;
import com.openclassrooms.SafetyNetAlerts.service.impl.MedicalRecordServiceImpl;

@RestController
public class MedicalRecordController {
	private static Logger logger = LogManager.getLogger(MedicalRecordController.class);
	
	@Autowired
	private MedicalRecordServiceImpl medicalRecordService;

	@PostMapping("/medicalRecord")
	@ResponseStatus(code = HttpStatus.CREATED)
	public void createMedicalRecord(@RequestBody MedicalRecord medicalRecord) {
        logger.info("POST /medicalRecord - creating {} {}", medicalRecord.getFirstName(), medicalRecord.getLastName());
        logger.debug("Payload received (medical details omitted)");
        try {
            medicalRecordService.add(medicalRecord);
            logger.info("POST /medicalRecord -> 201 Created ({} {})",
                    medicalRecord.getFirstName(), medicalRecord.getLastName());
        } catch (Exception e) {
            logger.error("POST /medicalRecord FAILED for {} {}: {}",
                    medicalRecord.getFirstName(), medicalRecord.getLastName(), e.getMessage(), e);
            throw e;
        }
    }
	
	@DeleteMapping("/medicalRecord")
	@ResponseStatus(code = HttpStatus.OK)
	public void deleteMedicalRecord(@RequestBody MedicalRecord medicalRecord) {
        logger.info("DELETE /medicalRecord - {} {}", medicalRecord.getFirstName(), medicalRecord.getLastName());
        try {
            medicalRecordService.delete(medicalRecord);
            logger.info("DELETE /medicalRecord -> 200 OK ({} {})",
                    medicalRecord.getFirstName(), medicalRecord.getLastName());
        } catch (Exception e) {
            logger.error("DELETE /medicalRecord FAILED for {} {}: {}",
                    medicalRecord.getFirstName(), medicalRecord.getLastName(), e.getMessage(), e);
            throw e;
        }
    }

    @PutMapping("/medicalRecord")
	@ResponseStatus(code = HttpStatus.OK)
	public void updateMedicalRecord(@RequestBody MedicalRecord medicalRecord) {
        logger.info("PUT /medicalRecord - {} {}", medicalRecord.getFirstName(), medicalRecord.getLastName());
        logger.debug("Payload received (medical details omitted)");
        try {
            medicalRecordService.update(medicalRecord);
            logger.info("PUT /medicalRecord -> 200 OK ({} {})",
                    medicalRecord.getFirstName(), medicalRecord.getLastName());
        } catch (Exception e) {
            logger.error("PUT /medicalRecord FAILED for {} {}: {}",
                    medicalRecord.getFirstName(), medicalRecord.getLastName(), e.getMessage(), e);
            throw e;
        }
    }
	
	@GetMapping("/medicalRecord")
	@ResponseStatus(code = HttpStatus.OK)
	public MedicalRecord getMedicalRecord(@RequestParam("firstName") String firstName,
										  @RequestParam("lastName") String lastName) {
        logger.info("GET /medicalRecord firstName='{}' lastName='{}'", firstName, lastName);
        try {
            MedicalRecord key = new MedicalRecord(firstName, lastName);
            MedicalRecord medicalRecord = medicalRecordService.get(key);
            if (medicalRecord == null) {
                logger.info("GET /medicalRecord -> 200 OK, result=empty ({} {})", firstName, lastName);
            } else {
                logger.info("GET /medicalRecord -> 200 OK (found=true)");
                logger.debug("Record loaded for {} {} (medical details omitted)", firstName, lastName);
            }
            return medicalRecord;
        } catch (Exception e) {
            logger.error("GET /medicalRecord FAILED for {} {}: {}", firstName, lastName, e.getMessage(), e);
            throw e;
        }
    }
	
	@GetMapping("/medicalRecords")
    public List<MedicalRecord> getMedicalRecordList() {
        logger.info("GET /medicalRecords");
        try {
            List<MedicalRecord> medicalRecordList = medicalRecordService.findAll();
            logger.info("GET /medicalRecords -> 200 OK, count={}", medicalRecordList.size());
            logger.debug("First results (details omitted): {}",
                    medicalRecordList.stream().limit(3).map(m -> m.getFirstName() + " " + m.getLastName()).toList());
            return medicalRecordList;
        } catch (Exception e) {
            logger.error("GET /medicalRecords FAILED: {}", e.getMessage(), e);
            throw e;
        }
    }
}

