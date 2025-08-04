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
	public void add(FireStation fireStation) {
		fireStationRepository.add(fireStation);
	}

	@Override
	public void delete(FireStation fireStation) {
		fireStationRepository.delete(fireStation);
	}

	@Override
	public void update(FireStation fireStation) {
		fireStationRepository.update(fireStation);
	}
	
	@Override
    public FireStation get(FireStation fireStation) {
        return fireStationRepository.get(fireStation);
    }

	@Override
	public List<FireStation> findAll() {
		return fireStationRepository.findAll();
	}
}