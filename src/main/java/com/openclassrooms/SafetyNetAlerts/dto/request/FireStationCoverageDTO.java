package com.openclassrooms.SafetyNetAlerts.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class FireStationCoverageDTO {

    private String station;

    private List<PersonByStationNumberDTO> infoPerson = new ArrayList<>();

    private int nbAdult;

    private int nbChild;
}
