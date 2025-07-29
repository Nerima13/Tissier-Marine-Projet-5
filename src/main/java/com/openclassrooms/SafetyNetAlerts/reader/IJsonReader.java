package com.openclassrooms.SafetyNetAlerts.reader;

import java.io.IOException;

import com.openclassrooms.SafetyNetAlerts.dto.DataDTO;

public interface IJsonReader {
	
	public DataDTO readJsonFile(String fileName) throws IOException;

}
