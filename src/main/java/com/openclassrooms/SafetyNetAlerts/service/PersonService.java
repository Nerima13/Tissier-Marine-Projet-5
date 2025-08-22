package com.openclassrooms.SafetyNetAlerts.service;

import com.openclassrooms.SafetyNetAlerts.dto.request.childAlert.ChildDTO;
import com.openclassrooms.SafetyNetAlerts.dto.request.fireAlert.FireAlertAddressDTO;
import com.openclassrooms.SafetyNetAlerts.dto.request.firestationNumber.FireStationCoverageDTO;
import com.openclassrooms.SafetyNetAlerts.dto.request.floodAlert.FloodAlertDTO;
import com.openclassrooms.SafetyNetAlerts.dto.request.personInfo.PersonInfoDTO;
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
