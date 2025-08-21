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

	private List<MedicalRecord> medicalRecords = new ArrayList<>();
	
	@Bean
	@Scope("singleton")
	public MedicalRecordRepositoryImpl medicalRecordRepoSingleton() {
		return new MedicalRecordRepositoryImpl();
	}
	
	@Override
	public void add(MedicalRecord medicalRecord) {
		medicalRecords.add(medicalRecord);
	}
	
	@Override
	public void delete(MedicalRecord medicalRecord) {
		medicalRecords.remove(medicalRecord);
	}
	
	@Override
	public void update(MedicalRecord medicalRecord) {
		medicalRecords.forEach(m -> {
	    	if (m.equals(medicalRecord)) {
	    		m.setBirthdate(medicalRecord.getBirthdate());
	            m.setMedications(medicalRecord.getMedications());
	            m.setAllergies(medicalRecord.getAllergies());
	        }
	    });
	}
	
	@Override
	public MedicalRecord get(MedicalRecord medicalRecord) {
		for (MedicalRecord m : medicalRecords) {
			if (m.equals(medicalRecord)) {
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
	public void addAll(List<MedicalRecord> element) {
		medicalRecords.addAll(element);
	}

    @Override
    public void clear() {
        medicalRecords.clear();
    }
}
