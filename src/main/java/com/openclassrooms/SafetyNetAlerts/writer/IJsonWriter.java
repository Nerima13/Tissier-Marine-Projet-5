package com.openclassrooms.SafetyNetAlerts.writer;

import com.openclassrooms.SafetyNetAlerts.dto.DataDTO;

import java.io.IOException;

public interface IJsonWriter {

    void writeJsonFile(DataDTO dto) throws IOException;
}
