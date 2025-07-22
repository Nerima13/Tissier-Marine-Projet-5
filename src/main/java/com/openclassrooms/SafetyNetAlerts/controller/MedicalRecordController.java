package com.openclassrooms.SafetyNetAlerts.controller;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import com.openclassrooms.SafetyNetAlerts.model.MedicalRecord;
import com.openclassrooms.SafetyNetAlerts.service.impl.MedicalRecordServiceImpl;

@RestController
public class MedicalRecordController {
	private static Logger logger = LogManager.getLogger(MedicalRecordController.class);
	
	@Autowired
	private MedicalRecordServiceImpl medicalRecordService;

	@PostMapping("/medicalRecord")
	public void createMedicalRecord(MedicalRecord medicalRecord) {
		logger.info("createMedicalRecord called");
		medicalRecordService.add(medicalRecord);
		logger.info("MedicalRecord successfully created" + medicalRecord.toString());
	}
	
	@DeleteMapping("/medicalRecord")
	public void deleteMedicalRecord(MedicalRecord medicalRecord) {
		logger.info("deleteMedicalRecord called");
		medicalRecordService.delete(medicalRecord);
		logger.info("MedicalRecord successfully deleted" + medicalRecord.toString());
	}
	
	@PutMapping("/medicalRecord")
	public void updateMedicalRecord(MedicalRecord medicalRecord) {
		logger.info("updateMedicalRecord called");
		medicalRecordService.update(medicalRecord);
		logger.info("MedicalRecord successfully updated" + medicalRecord.toString());
	}
	
	@GetMapping("/medicalRecord")
	public MedicalRecord getMedicalRecord(MedicalRecord medicalRecord) {
		logger.info("getMedicalRecord called");
		medicalRecordService.get(medicalRecord);
		logger.info("MedicalRecord successfully gotten" + medicalRecord.toString());
		return medicalRecord;
	}
	
	@GetMapping("/all")
    public List<MedicalRecord> getMedicalRecordList() {
        logger.info("getMedicalRecordList called");
        List<MedicalRecord> medicalRecordList = medicalRecordService.findAll();
        logger.info("MedicalRecord list successfully gotten" + medicalRecordList.toString());
        return medicalRecordList;
    }
}

