package com.openclassrooms.SafetyNetAlerts.dto;

import java.util.List;

import com.openclassrooms.SafetyNetAlerts.model.FireStation;
import com.openclassrooms.SafetyNetAlerts.model.MedicalRecord;
import com.openclassrooms.SafetyNetAlerts.model.Person;

import lombok.Data;

@Data
public class DataDTO {
	private List<Person> persons;
	private List<FireStation> firestations;
	private List<MedicalRecord> medicalrecords;

}
