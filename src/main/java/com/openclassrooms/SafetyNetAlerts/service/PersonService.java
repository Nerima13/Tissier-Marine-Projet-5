package com.openclassrooms.SafetyNetAlerts.service;

import com.openclassrooms.SafetyNetAlerts.dto.request.*;
import com.openclassrooms.SafetyNetAlerts.model.Person;

import java.util.List;

public interface PersonService extends CrudService<Person> {


    FireStationCoverageDTO getPersonsCoveredByStation(int station);

    ChildDTO getChildInfos(String address);

    List<String> getPhoneByFireStation(String fireStationNumber);

    FireAlertDTO getFireAlert(String address);

    FloodAlertDTO getFloodAlert(List<String> stations);

    PersonInfoDTO getPersonInfo(String firstName, String lastName);

    List<String> getEmail(String city);
}
