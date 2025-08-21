package com.openclassrooms.SafetyNetAlerts.dto.request.fireAlert;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class FireAlertAddressDTO {

    private String station;

    private List<FireAlertPersonDTO> residents = new ArrayList<>();
}
