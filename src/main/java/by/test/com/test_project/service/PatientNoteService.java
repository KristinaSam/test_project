package by.test.com.test_project.service;
import by.test.com.test_project.dto.NoteDTO;
import by.test.com.test_project.model.entity.CompanyUser;
import by.test.com.test_project.model.entity.PatientNote;
import by.test.com.test_project.model.entity.PatientProfile;

import java.util.List;
import java.util.Optional;

public interface PatientNoteService {

        void updateNote(PatientNote existingNote, PatientProfile patient, CompanyUser user, NoteDTO noteDTO);
        PatientNote createNote(PatientProfile patient, CompanyUser user, NoteDTO noteDTO);
        void importOrUpdateNote(PatientProfile patient, CompanyUser user, NoteDTO noteDTO);
        List<PatientNote> findNotesByPatientAndGuid(String guid);
}
