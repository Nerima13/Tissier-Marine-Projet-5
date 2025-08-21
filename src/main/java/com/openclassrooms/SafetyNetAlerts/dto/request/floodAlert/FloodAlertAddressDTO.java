package com.openclassrooms.SafetyNetAlerts.dto.request.floodAlert;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class FloodAlertAddressDTO {

    private String address;

    private final List<FloodAlertPersonDTO> listPerson = new ArrayList<>();
}
