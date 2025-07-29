package com.openclassrooms.SafetyNetAlerts.reader;

import java.io.IOException;
import java.io.InputStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.SafetyNetAlerts.dto.DataDTO;

public class ReadDataFromJson implements IJsonReader {
	
	private Resource resource = new ClassPathResource("data.json");
	private static Logger logger = LogManager.getLogger(ReadDataFromJson.class);
	
	@Override
	public DataDTO readJsonFile(String fileName) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		try (InputStream inputStream = resource.getInputStream()) {
			return mapper.readValue(inputStream, DataDTO.class);
		} catch (IOException e) {
			logger.error("Error reading JSON file : " + fileName, e);
			throw e;
		}
	}
}
