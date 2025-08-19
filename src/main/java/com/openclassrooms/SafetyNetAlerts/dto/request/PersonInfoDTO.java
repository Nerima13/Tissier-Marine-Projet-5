package com.openclassrooms.SafetyNetAlerts.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class PersonInfoDTO {

    private String firstName;

    private String lastName;

    private int age;

    private String address;

    private String email;

    private List<String> medications = new ArrayList<>();

    private List<String> allergies = new ArrayList<>();
}
