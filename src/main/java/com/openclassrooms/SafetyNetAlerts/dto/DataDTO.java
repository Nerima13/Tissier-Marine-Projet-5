package com.openclassrooms.SafetyNetAlerts.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.openclassrooms.SafetyNetAlerts.model.FireStation;
import com.openclassrooms.SafetyNetAlerts.model.MedicalRecord;
import com.openclassrooms.SafetyNetAlerts.model.Person;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class DataDTO {
	private List<Person> persons;
	private List<FireStation> firestations;
	private List<MedicalRecord> medicalrecords;

    @JsonIgnore
	public List<Person> getListPersons() {
		return persons;
	}

    @JsonIgnore
	public List<MedicalRecord> getListMedicalRecords() {
		return medicalrecords;
	}

    @JsonIgnore
	public List<FireStation> getListFireStations() {
		return firestations;
	}
}