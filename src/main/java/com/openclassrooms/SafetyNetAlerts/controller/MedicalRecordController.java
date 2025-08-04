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
		logger.info("createMedicalRecord called");
		medicalRecordService.add(medicalRecord);
		logger.info("MedicalRecord successfully created" + medicalRecord.toString());
	}
	
	@DeleteMapping("/medicalRecord")
	@ResponseStatus(code = HttpStatus.OK)
	public void deleteMedicalRecord(@RequestBody MedicalRecord medicalRecord) {
		logger.info("deleteMedicalRecord called");
		medicalRecordService.delete(medicalRecord);
		logger.info("MedicalRecord successfully deleted" + medicalRecord.toString());
	}
	
	@PutMapping("/medicalRecord")
	@ResponseStatus(code = HttpStatus.OK)
	public void updateMedicalRecord(@RequestBody MedicalRecord medicalRecord) {
		logger.info("updateMedicalRecord called");
		medicalRecordService.update(medicalRecord);
		logger.info("MedicalRecord successfully updated" + medicalRecord.toString());
	}
	
	@GetMapping("/medicalRecord")
	@ResponseStatus(code = HttpStatus.OK)
	public MedicalRecord getMedicalRecord(@RequestParam("firstName") String firstName,
										  @RequestParam("lastName") String lastName) {
		logger.info("getMedicalRecord called");
		MedicalRecord medicalRecord = new MedicalRecord(firstName, lastName);
		medicalRecord = medicalRecordService.get(medicalRecord);
		logger.info("MedicalRecord response : " + medicalRecord.toString());
		return medicalRecord;
	}
	
	@GetMapping("/medicalRecords")
    public List<MedicalRecord> getMedicalRecordList() {
        logger.info("getMedicalRecordList called");
        List<MedicalRecord> medicalRecordList = medicalRecordService.findAll();
        logger.info("MedicalRecord list response : " + medicalRecordList.toString());
        return medicalRecordList;
    }
}

