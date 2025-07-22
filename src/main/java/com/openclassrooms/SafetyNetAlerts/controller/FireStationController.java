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

import com.openclassrooms.SafetyNetAlerts.model.FireStation;
import com.openclassrooms.SafetyNetAlerts.service.impl.FireStationServiceImpl;

@RestController
public class FireStationController {
	
	private static Logger logger = LogManager.getLogger(FireStationController.class);
		
	@Autowired
	private FireStationServiceImpl fireStationService;
	
	@PostMapping("/firestation")
	public void createFireStation(FireStation fireStation) {
		logger.info("createFireStation called");
		fireStationService.add(fireStation);
		logger.info("Firestation successfully created" + fireStation.toString());
	}
		
	@DeleteMapping("/firestation")
	public void deleteFireStation(FireStation fireStation) {
		logger.info("deleteFireStation called");
		fireStationService.delete(fireStation);
		logger.info("Firestation successfully deleted" + fireStation.toString());
	}
		
	@PutMapping("/firestation")
	public void updateFireStation(FireStation fireStation) {
		logger.info("updateFireStation called");
		fireStationService.update(fireStation);
		logger.info("Firestation successfully updated" + fireStation.toString());
	}
		
	@GetMapping("/firestation")
	public FireStation getFireStation(FireStation fireStation) {
		logger.info("getFireStation called");
		fireStationService.get(fireStation);
		logger.info("Firestation successfully gotten" + fireStation.toString());
		return fireStation;
	}
		
	@GetMapping("/all")
	public List<FireStation> getFireStationList() {
	    logger.info("getFireStationList called");
	    List<FireStation> fireStationList = fireStationService.findAll();
	    logger.info("Firestation list successfully gotten" + fireStationList.toString());
	    return fireStationList;
	}
}

