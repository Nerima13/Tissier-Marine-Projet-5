package com.openclassrooms.SafetyNetAlerts.service;

import com.openclassrooms.SafetyNetAlerts.dto.request.*;
import com.openclassrooms.SafetyNetAlerts.model.Person;

import java.util.List;

public interface PersonService extends CrudService<Person> {


    FireStationCoverageDTO getPersonsCoveredByStation(String station);

    List<ChildDTO> getChildInfos(String address);

    List<String> getPhoneByFireStation(String fireStationNumber);

    FireAlertAddressDTO getFireAlert(String address);

    FloodAlertDTO getFloodAlert(List<String> stations);

    List<PersonInfoDTO> getPersonInfo(String lastName);

    List<String> getEmail(String city);
}
