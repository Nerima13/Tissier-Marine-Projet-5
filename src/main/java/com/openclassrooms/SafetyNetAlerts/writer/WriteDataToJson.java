package com.openclassrooms.SafetyNetAlerts.writer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.openclassrooms.SafetyNetAlerts.dto.DataDTO;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;

@Component
public class WriteDataToJson implements IJsonWriter {

    private final ObjectMapper mapper = new ObjectMapper();

    private static final String resource = "src/main/resources/data.json";

    @Override
    public void writeJsonFile(DataDTO dto) throws IOException {
        if (dto == null) {
            return;
        }
        mapper.writerWithDefaultPrettyPrinter()
                .writeValue(new File(resource), dto);
    }
}
