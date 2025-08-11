package com.openclassrooms.SafetyNetAlerts.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class FloodAlertPersonDTO {

    private String firstName;

    private String lastName;

    private String phone;

    private int age;

    private List<String> medications = new ArrayList<>();

    private List<String> allergies = new ArrayList<>();
}
