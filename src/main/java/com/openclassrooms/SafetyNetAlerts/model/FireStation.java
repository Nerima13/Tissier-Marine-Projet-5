package com.openclassrooms.SafetyNetAlerts.model;

import lombok.Data;

@Data
public class FireStation {
	private String address;
	private int station;
	
	public FireStation(String address, int station) {
		this.address = address;
		this.station = station;
	}
	
	public FireStation() {
	}
}
