package com.openclassrooms.SafetyNetAlerts.repository.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import com.openclassrooms.SafetyNetAlerts.model.MedicalRecord;
import com.openclassrooms.SafetyNetAlerts.repository.MedicalRecordRepository;

@Repository
public class MedicalRecordRepositoryImpl implements MedicalRecordRepository {
	
	private static Logger logger = LogManager.getLogger(MedicalRecordRepositoryImpl.class);
	private List<MedicalRecord> medicalRecords = new ArrayList<>();
	
	@Bean
	@Scope("singleton")
	public MedicalRecordRepositoryImpl medicalRecordRepoSingleton() {
		return new MedicalRecordRepositoryImpl();
	}
	
	@Override
	public void add(MedicalRecord element) {
		medicalRecords.add(element);
	}
	
	@Override
	public void delete(MedicalRecord element) {
		medicalRecords.remove(element);
	}
	
	@Override
	public void update(MedicalRecord element) {
		medicalRecords.forEach(m -> {
	    	if (m.equals(element)) {
	    		m.setBirthdate(element.getBirthdate());
	            m.setMedications(element.getMedications());
	            m.setAllergies(element.getAllergies());
	        }
	    });
	}
	
	@Override
	public MedicalRecord get(MedicalRecord element) {
		for (MedicalRecord m : medicalRecords) {
			if (m.equals(element)) {
	            return m;
	        }
	    }
	    return null;
	}
	
	@Override
	public List<MedicalRecord> findAll() {
		return medicalRecords;
	}

	@Override
	public void addAll(List<MedicalRecord> elements) {
elements.addAll(medicalRecords)	;	
	}

}
