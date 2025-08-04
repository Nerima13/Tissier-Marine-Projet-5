package com.openclassrooms.SafetyNetAlerts.dto;

import java.util.ArrayList;
import java.util.List;

import com.openclassrooms.SafetyNetAlerts.model.FireStation;
import com.openclassrooms.SafetyNetAlerts.model.MedicalRecord;
import com.openclassrooms.SafetyNetAlerts.model.Person;

import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class DataDTO {
	private List<Person> persons;
	private List<FireStation> firestations;
	private List<MedicalRecord> medicalrecords;


	public DataDTO(ArrayList<Person> listPersons, ArrayList<FireStation> listFireStations, ArrayList<MedicalRecord> listMedicalRecords) {
		this.persons = listPersons;
		this.firestations = listFireStations;
		this.medicalrecords = listMedicalRecords;
	}

	public List<Person> getListPersons() {
		return persons;
	}

	public List<MedicalRecord> getListMedicalRecords() {
		return medicalrecords;
	}

	public List<FireStation> getListFireStations() {
		return firestations;
	}
}