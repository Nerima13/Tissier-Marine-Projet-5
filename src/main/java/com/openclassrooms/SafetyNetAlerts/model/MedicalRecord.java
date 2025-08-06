package com.openclassrooms.SafetyNetAlerts.model;

import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

@EqualsAndHashCode
public class MedicalRecord {
	
	private String firstName;
	
	private String lastName;
	
	@EqualsAndHashCode.Exclude
	private String birthdate;
	
	@EqualsAndHashCode.Exclude
	private List<String> medications;
	
	@EqualsAndHashCode.Exclude
	private List<String> allergies;

	public MedicalRecord(String firstName, String lastName) {
		this.firstName = firstName;
		this.lastName = lastName;
	}
}
