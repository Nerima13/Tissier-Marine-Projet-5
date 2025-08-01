package com.openclassrooms.SafetyNetAlerts.reader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.SafetyNetAlerts.dto.DataDTO;
import com.openclassrooms.SafetyNetAlerts.model.FireStation;
import com.openclassrooms.SafetyNetAlerts.model.MedicalRecord;
import com.openclassrooms.SafetyNetAlerts.model.Person;

public class ReadDataFromJson implements IJsonReader {

	private Resource resource = new ClassPathResource("data.json");
	private static Logger logger = LogManager.getLogger(ReadDataFromJson.class);

	@Override
	public DataDTO readJsonFile() throws IOException {
		if (resource == null) {
			logger.error("There was a problem when reading data.json");
			return new DataDTO();
		}

		ArrayList<Person> listPersons = new ArrayList<>();
		ArrayList<FireStation> listFireStations = new ArrayList<>();
		ArrayList<MedicalRecord> listMedicalRecords = new ArrayList<>();

		ObjectMapper mapper = new ObjectMapper();

		JsonNode jsonNode = mapper.readTree(resource.getFile());

		jsonNode.get("persons").forEach(node -> addDeserializeNodeToList(listPersons, node, Person.class));
		jsonNode.get("firestations").forEach(node -> addDeserializeNodeToList(listFireStations, node, FireStation.class));
		jsonNode.get("medicalrecords").forEach(node -> addDeserializeNodeToList(listMedicalRecords, node, MedicalRecord.class));

		DataDTO jsonListDTO = new DataDTO(listPersons, listFireStations, listMedicalRecords);

		return jsonListDTO;
	}

	private <T> T deserialize(JsonNode node, Class<T> valueType)
			throws JsonProcessingException, IllegalArgumentException {
		ObjectMapper mapper = new ObjectMapper();
		T object = mapper.treeToValue(node, valueType);
		return object;
	}

	private <T> void addDeserializeNodeToList(List<T> list, JsonNode node, Class<T> valueType) {
		try {
			list.add(deserialize(node, valueType));
		} catch (JsonProcessingException | IllegalArgumentException e) {
			logger.error(e.getMessage());
		}
	}
}