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
	@ResponseStatus(code = HttpStatus.CREATED)
	public void createFireStation(@RequestBody FireStation fireStation) {
        logger.info("POST /firestation - add mapping {} -> station {}", fireStation.getAddress(), fireStation.getStation());
        logger.debug("Payload: {}", fireStation);
        try {
            fireStationService.add(fireStation);
            logger.info("POST /firestation -> 201 Created");
        } catch (Exception e) {
            logger.error("POST /firestation FAILED for mapping {} -> {}: {}", fireStation.getAddress(), fireStation.getStation(), e.getMessage(), e);
            throw e;
        }
    }
		
	@DeleteMapping("/firestation")
	@ResponseStatus(code = HttpStatus.OK)
	public void deleteFireStation(@RequestBody FireStation fireStation) {
        logger.info("DELETE /firestation - {} -> station {}",
                fireStation.getAddress(), fireStation.getStation());
        logger.debug("Payload: {}", fireStation);
        try {
            fireStationService.delete(fireStation);
            logger.info("DELETE /firestation -> 200 OK");
        } catch (Exception e) {
            logger.error("DELETE /firestation FAILED for mapping {} -> {}: {}",
                    fireStation.getAddress(), fireStation.getStation(), e.getMessage(), e);
            throw e;
        }
    }
		
	@PutMapping("/firestation")
	@ResponseStatus(code = HttpStatus.OK)
	public void updateFireStation(@RequestBody FireStation fireStation) {
        logger.info("PUT /firestation - {} -> station {}",
                fireStation.getAddress(), fireStation.getStation());
        logger.debug("Payload: {}", fireStation);
        try {
            fireStationService.update(fireStation);
            logger.info("PUT /firestation -> 200 OK");
        } catch (Exception e) {
            logger.error("PUT /firestation FAILED for mapping {} -> {}: {}",
                    fireStation.getAddress(), fireStation.getStation(), e.getMessage(), e);
            throw e;
        }
    }
		
	@GetMapping("/firestation")
	@ResponseStatus(code = HttpStatus.OK)
	public FireStation getFireStation(@RequestParam("address") String address) {
        logger.info("GET /firestation?address={}", address);
        try {
            FireStation fireStation = fireStationService.get(new FireStation(address, null));
            if (fireStation == null) {
                logger.info("GET /firestation -> 200 OK, result=empty (address={})", address);
            } else {
                logger.info("GET /firestation -> 200 OK (found=true)");
                logger.debug("Result: {}", fireStation);
            }
            return fireStation;
        } catch (Exception e) {
            logger.error("GET /firestation FAILED for address {}: {}", address, e.getMessage(), e);
            throw e;
        }
    }
		
	@GetMapping("/firestations")
	public List<FireStation> getFireStationList() {
        logger.info("GET /firestations");
        try {
            List<FireStation> list = fireStationService.findAll();
            logger.info("GET /firestations -> 200 OK, count={}", list.size());
            logger.debug("First results: {}", list.stream().limit(3).toList());
            return list;
        } catch (Exception e) {
            logger.error("GET /firestations FAILED: {}", e.getMessage(), e);
            throw e;
        }
    }
}


