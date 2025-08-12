package by.test.com.test_project.service.impl;

import by.test.com.test_project.dto.NoteDTO;
import by.test.com.test_project.model.entity.CompanyUser;
import by.test.com.test_project.model.entity.PatientNote;
import by.test.com.test_project.model.entity.PatientProfile;
import by.test.com.test_project.model.repository.PatientNoteRepository;
import by.test.com.test_project.service.PatientNoteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PatientNoteServiceImpl implements PatientNoteService {

    private final PatientNoteRepository patientNoteRepository;

    @Override
    public void updateNote(PatientNote existingNote, PatientProfile patient, CompanyUser user, NoteDTO noteDTO) {
        existingNote.setNote(noteDTO.comments());
        existingNote.setLastModifiedDateTime(noteDTO.modifiedDateTime());
        existingNote.setLastModifiedByUser(user);
        patientNoteRepository.save(existingNote);
    }

    @Override
    public PatientNote createNote(PatientProfile patient, CompanyUser user, NoteDTO noteDTO) {
        PatientNote patientNote = new PatientNote();
        patientNote.setNote(noteDTO.comments());
        patientNote.setCreatedDateTime(noteDTO.createdDateTime());
        patientNote.setLastModifiedDateTime(noteDTO.modifiedDateTime());
        patientNote.setCreatedByUser(user);
        patientNote.setLastModifiedByUser(user);
        patientNote.setPatientProfile(patient);
        return patientNoteRepository.save(patientNote);
    }

    @Override
    public void importOrUpdateNote(PatientProfile patient, CompanyUser user, NoteDTO noteDTO) {
        Optional<PatientNote> existingNoteOpt = patientNoteRepository.findByPatientProfile_OldClientGuidContaining(noteDTO.guid());
        if (existingNoteOpt.isPresent()) {
            PatientNote existingNote = existingNoteOpt.get();
            if (existingNote.getLastModifiedDateTime().isBefore(noteDTO.modifiedDateTime())) {
                updateNote(existingNote, patient, user, noteDTO);
            }
        } else {
            createNote(patient, user, noteDTO);
        }
    }

    @Override
    public List<PatientNote> findNotesByPatientAndGuid(String guid) {
        return patientNoteRepository.findByPatientProfileOldClientGuid(guid);
    }

}
