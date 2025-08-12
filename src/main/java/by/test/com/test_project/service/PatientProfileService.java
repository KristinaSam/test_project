package by.test.com.test_project.service;

import by.test.com.test_project.dto.ClientDTO;
import by.test.com.test_project.model.entity.PatientProfile;

import java.util.List;

public interface PatientProfileService {
    PatientProfile findByOldClientGuid(String oldClientGuid);
    PatientProfile createPatientProfile(ClientDTO clientDTO);
    List<PatientProfile> findActivePatientsByOldClientGuid(String oldClientGuid);
}
