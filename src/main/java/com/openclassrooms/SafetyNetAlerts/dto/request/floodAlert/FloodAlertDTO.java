package com.openclassrooms.SafetyNetAlerts.dto.request.floodAlert;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class FloodAlertDTO {

    private List<String> stations;

    private List<FloodAlertAddressDTO> addressList = new ArrayList<>();

}
