package com.openclassrooms.SafetyNetAlerts.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

@EqualsAndHashCode
public class Person {
	
	private String firstName;
	
	private String lastName;
	
	@EqualsAndHashCode.Exclude
	private String address;
	
	@EqualsAndHashCode.Exclude
	private String city;
	
	@EqualsAndHashCode.Exclude
	private int zip;
	
	@EqualsAndHashCode.Exclude
	private String phone;
	
	@EqualsAndHashCode.Exclude
	private String email;
	
}
