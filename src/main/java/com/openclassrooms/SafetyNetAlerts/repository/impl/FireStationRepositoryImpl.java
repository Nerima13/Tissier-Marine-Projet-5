package com.openclassrooms.SafetyNetAlerts.repository.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import com.openclassrooms.SafetyNetAlerts.model.FireStation;
import com.openclassrooms.SafetyNetAlerts.repository.FireStationRepository;

@Repository
public class FireStationRepositoryImpl implements FireStationRepository {

	private List<FireStation> fireStations = new ArrayList<>();
	
	@Bean
	@Scope("singleton")
	public FireStationRepositoryImpl fireStationRepoSingleton() {
		return new FireStationRepositoryImpl();
	}
	
	@Override
	public void add(FireStation fireStation) {
		fireStations.add(fireStation);
	}
	
	@Override
	public void delete(FireStation fireStation) {
		fireStations.remove(fireStation);
	}
	
	@Override
	public void update(FireStation fireStation) {
		fireStations.forEach(f -> {
	    	if (f.equals(fireStation)) {
	            f.setStation(fireStation.getStation());
	        }
	    });
	}
	
	@Override
	public FireStation get(FireStation fireStation) {
	    for (FireStation f : fireStations) {
	    	if (f.equals(fireStation)) {
	            return f;
	        }
	    }
	    return null;
	}
	
	@Override
	public List<FireStation> findAll() {
		return fireStations;
	}

	@Override
	public void addAll(List<FireStation> element) {
		fireStations.addAll(element);
	}

    @Override
    public void clear() {
        fireStations.clear();
    }
}
