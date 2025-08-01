package com.openclassrooms.SafetyNetAlerts.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.openclassrooms.SafetyNetAlerts.model.MedicalRecord;
import com.openclassrooms.SafetyNetAlerts.repository.MedicalRecordRepository;
import com.openclassrooms.SafetyNetAlerts.service.MedicalRecordService;

@Service
public class MedicalRecordServiceImpl implements MedicalRecordService {
	
	@Autowired
	@Qualifier("medicalRecordRepoSingleton")
	private MedicalRecordRepository medicalRecordRepository;

	@Override
	public void add(MedicalRecord medicalRecord) {
		medicalRecordRepository.add(medicalRecord);
	}

	@Override
	public void delete(MedicalRecord element) {
		medicalRecordRepository.delete(element);
	}

	@Override
	public void update(MedicalRecord element) {
		medicalRecordRepository.update(element);
	}
	
	@Override
    public MedicalRecord get(MedicalRecord element) {
        return medicalRecordRepository.get(element); 
    }

	@Override
	public List<MedicalRecord> findAll() {
		return medicalRecordRepository.findAll();
	}
}
