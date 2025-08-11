package com.openclassrooms.SafetyNetAlerts.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

@EqualsAndHashCode
public class FireStation {
	
	private String address;
	
	@EqualsAndHashCode.Exclude
	private String station;

	public FireStation(String address) {
		this.address = address;
	}
}
