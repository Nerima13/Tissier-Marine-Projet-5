package com.openclassrooms.SafetyNetAlerts.dto.request.firestationNumber;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class PersonByStationNumberDTO {

    private String firstName;

    private String lastName;

    private String address;

    private String phone;
}
