package com.openclassrooms.SafetyNetAlerts.controller;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.openclassrooms.SafetyNetAlerts.model.FireStation;
import com.openclassrooms.SafetyNetAlerts.service.impl.FireStationServiceImpl;

@RestController
public class FireStationController {
	
	private static Logger logger = LogManager.getLogger(FireStationController.class);
		
	@Autowired
	private FireStationServiceImpl fireStationService;
	
	@PostMapping("/firestation")
	public void createFireStation(@RequestBody FireStation fireStation) {
		logger.info("createFireStation called");
		fireStationService.add(fireStation);
		logger.info("Firestation successfully created" + fireStation.toString());
	}
		
	@DeleteMapping("/firestation")
	public void deleteFireStation(@RequestBody FireStation fireStation) {
		logger.info("deleteFireStation called");
		fireStationService.delete(fireStation);
		logger.info("Firestation successfully deleted" + fireStation.toString());
	}
		
	@PutMapping("/firestation")
	public void updateFireStation(@RequestBody FireStation fireStation) {
		logger.info("updateFireStation called");
		fireStationService.update(fireStation);
		logger.info("Firestation successfully updated" + fireStation.toString());
	}
		
	@GetMapping("/firestation")
	@ResponseStatus(code = HttpStatus.OK)
	public FireStation getFireStation(@RequestParam("address") String address) {
		logger.info("getFireStation called");
		FireStation fireStation = new FireStation(address);
		fireStation = fireStationService.get(fireStation);
		logger.info("Firestation response : " + fireStation.toString());
		return fireStation;
	}
		
	@GetMapping("/firestations")
	public List<FireStation> getFireStationList() {
	    logger.info("getFireStationList called");
	    List<FireStation> fireStationList = fireStationService.findAll();
	    logger.info("Firestation list response : " + fireStationList.toString());
	    return fireStationList;
	}
}

