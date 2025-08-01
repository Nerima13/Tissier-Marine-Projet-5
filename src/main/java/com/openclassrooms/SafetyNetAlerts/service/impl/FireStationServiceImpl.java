package com.openclassrooms.SafetyNetAlerts.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.openclassrooms.SafetyNetAlerts.model.FireStation;
import com.openclassrooms.SafetyNetAlerts.repository.FireStationRepository;
import com.openclassrooms.SafetyNetAlerts.service.FireStationService;

@Service
public class FireStationServiceImpl implements FireStationService {
	
	@Autowired
	@Qualifier("fireStationRepoSingleton")
	private FireStationRepository fireStationRepository;
	
	@Override
	public void add(FireStation element) {
		fireStationRepository.add(element);
	}

	@Override
	public void delete(FireStation element) {
		fireStationRepository.delete(element);
	}

	@Override
	public void update(FireStation element) {
		fireStationRepository.update(element);
	}
	
	@Override
    public FireStation get(FireStation element) {
        return fireStationRepository.get(element); 
    }

	@Override
	public List<FireStation> findAll() {
		return fireStationRepository.findAll();
	}
}