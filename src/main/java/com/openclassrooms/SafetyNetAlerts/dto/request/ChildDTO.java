package com.openclassrooms.SafetyNetAlerts.dto.request;

import com.openclassrooms.SafetyNetAlerts.model.Person;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class ChildDTO {

    private String firstName;

    private String lastName;

    private int age;

    private List<Person> family = new ArrayList<>();
}
