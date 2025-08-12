package by.test.com.test_project.service.impl;

import by.test.com.test_project.dto.ClientDTO;
import by.test.com.test_project.model.entity.PatientProfile;
import by.test.com.test_project.model.repository.PatientProfileRepository;
import by.test.com.test_project.service.PatientProfileService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PatientProfileServiceImpl implements PatientProfileService {

    private final PatientProfileRepository patientProfileRepository;

    @Override
    public PatientProfile findByOldClientGuid(String oldClientGuid) {
        return patientProfileRepository.findByOldClientGuid(oldClientGuid)
                .orElseThrow(() -> new EntityNotFoundException("Patient not found for old client guid: " + oldClientGuid));
    }

    @Override
    public PatientProfile createPatientProfile(ClientDTO clientDTO) {
        PatientProfile patient = new PatientProfile();
        patient.setOldClientGuid(clientDTO.guid());
        patient.setFirstName(clientDTO.firstName());
        patient.setLastName(clientDTO.lastName());
        patient.setStatusId((short) 200); // статус Active
        return patientProfileRepository.save(patient);
    }

    @Override
    public List<PatientProfile> findActivePatientsByOldClientGuid(String oldClientGuid) {
        return patientProfileRepository.findActiveByOldClientGuid(oldClientGuid);
    }
}
