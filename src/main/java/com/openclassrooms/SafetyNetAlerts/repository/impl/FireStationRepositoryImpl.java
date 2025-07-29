package com.openclassrooms.SafetyNetAlerts.repository.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.openclassrooms.SafetyNetAlerts.model.FireStation;
import com.openclassrooms.SafetyNetAlerts.repository.FireStationRepository;

@Repository
public class FireStationRepositoryImpl implements FireStationRepository {
	
	private static Logger logger = LogManager.getLogger(FireStationRepositoryImpl.class);
	private List<FireStation> fireStations = new ArrayList<>();
	
	@Override
	public void add(FireStation element) {
		fireStations.add(element);
	}
	
	@Override
	public void delete(FireStation element) {
		fireStations.remove(element);
	}
	
	@Override
	public void update(FireStation element) {
		fireStations.forEach(f -> {
	    	if (f.equals(element)) {
	            f.setStation(element.getStation());
	        }
	    });
	}
	
	@Override
	public FireStation get(FireStation element) {
	    for (FireStation f : fireStations) {
	    	if (f.equals(element)) {
	            return f;
	        }
	    }
	    return null;
	}
	
	@Override
	public List<FireStation> findAll() {
		return fireStations;
	}

}
