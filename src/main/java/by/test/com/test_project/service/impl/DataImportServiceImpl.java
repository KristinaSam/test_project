package by.test.com.test_project.service.impl;

import by.test.com.test_project.dto.ClientDTO;
import by.test.com.test_project.dto.NoteDTO;
import by.test.com.test_project.dto.NoteRequest;
import by.test.com.test_project.model.entity.CompanyUser;
import by.test.com.test_project.model.entity.PatientNote;
import by.test.com.test_project.model.entity.PatientProfile;
import by.test.com.test_project.service.CompanyUserService;
import by.test.com.test_project.service.DataImportService;
import by.test.com.test_project.service.PatientNoteService;
import by.test.com.test_project.service.PatientProfileService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class DataImportServiceImpl implements DataImportService {
    private final CompanyUserService companyUserService;
    private final PatientNoteService patientNoteService;
    private final PatientProfileService patientProfileService;
    private final RestTemplate restTemplate;
    private static final String OLD_SYSTEM_API_URL = "http://localhost:8081";


    @Override
    public void importNotes() {
        log.info("== Начало импорта данных из старой системы ==");
        try {
            List<ClientDTO> clients = getClientsFromOldSystem();
            log.info("Получено {} клиентов из старой системы", clients.size());

            for (ClientDTO client : clients) {
                List<PatientProfile> activePatients = patientProfileService.findActivePatientsByOldClientGuid(client.guid());
                log.info("Получено {} пациентов в новой системе", activePatients.size());

                if (!activePatients.isEmpty()) {
                    for (PatientProfile patient : activePatients) {
                        List<NoteDTO> notes = getNotesFromOldSystem(client.guid(), client.agency());

                        if (notes != null && !notes.isEmpty()) {
                            for (NoteDTO noteDTO : notes) {
                                CompanyUser user = findOrCreateUser(noteDTO.loggedUser());

                                List<PatientNote> existingNotes = patientNoteService.findNotesByPatientAndGuid(noteDTO.guid());

                                PatientNote existingNote = findMatchingNoteForUpdate(existingNotes, noteDTO);
                                if (existingNote != null) {
                                    if (isNewerNote(existingNote, noteDTO)) {
                                        log.info("Обновление заметки с GUID {}", noteDTO.guid());
                                        patientNoteService.updateNote(existingNote, patient, user, noteDTO);
                                    } else {
                                        log.info("Заметка с GUID {} уже актуальна, пропускаем импорт", noteDTO.guid());
                                    }
                                } else {
                                    log.info("Импорт новой заметки с GUID {}", noteDTO.guid());
                                    patientNoteService.importOrUpdateNote(patient, user, noteDTO);
                                }
                            }
                        }
                    }
                }
            }
            logImportStats(clients.size());
        } catch (Exception e) {
            logError(e);
        }
    }

    private boolean isActiveStatus(String status) {
        return "ACTIVE".equalsIgnoreCase(status);
    }

    private List<NoteDTO> getNotesFromOldSystem(String clientGuid, String agency) {
        try {
            NoteRequest noteRequest = new NoteRequest(agency, clientGuid, "2019-01-01", "2025-01-01");
            ResponseEntity<List<NoteDTO>> response = restTemplate.exchange(
                    OLD_SYSTEM_API_URL + "/notes",
                    HttpMethod.POST,
                    new HttpEntity<>(noteRequest),
                    new ParameterizedTypeReference<List<NoteDTO>>() {
                    }
            );
            return response.getBody() != null ? response.getBody() : new ArrayList<>();
        } catch (Exception e) {
            log.error("Ошибка при получении заметок для клиента с GUID {}", clientGuid, e);
            return new ArrayList<>();
        }
    }

    private CompanyUser findOrCreateUser(String login) {
        try {
            return companyUserService.findByLogin(login);
        } catch (EntityNotFoundException exception) {
            return companyUserService.createUser(login);
        }
    }

    private void logImportStats(int clientsImported) {
        System.out.println("Импорт завершен: " + clientsImported + " клиентов обработано.");
    }

    private void logError(Exception e) {
        System.err.println("Ошибка при импорте данных: " + e.getMessage());
    }

    private PatientNote findMatchingNoteForUpdate(List<PatientNote> existingNotes, NoteDTO noteDTO) {
        return existingNotes.stream()
                .filter(note -> note.getPatientProfile().getOldClientGuid().equals(noteDTO.clientGuid()) && note.getLastModifiedDateTime().isBefore(noteDTO.modifiedDateTime()))
                .findFirst()
                .orElse(null);
    }

    private boolean isNewerNote(PatientNote existingNote, NoteDTO newNote) {
        return existingNote.getLastModifiedDateTime().isBefore(newNote.modifiedDateTime());
    }

    private List<ClientDTO> getClientsFromOldSystem() {
        try {
            ResponseEntity<List<ClientDTO>> response = restTemplate.exchange(
                    OLD_SYSTEM_API_URL + "/clients",
                    HttpMethod.POST,
                    null,
                    new ParameterizedTypeReference<List<ClientDTO>>() {
                    }
            );

            if (response.getBody() != null) {
                return response.getBody().stream()
                        .filter(client -> isActiveStatus(client.status())) // Проверка на активность
                        .collect(Collectors.toList());
            }

            return new ArrayList<>();
        } catch (Exception e) {
            log.error("Ошибка при получении клиентов из старой системы", e);
            return new ArrayList<>();
        }
    }
}
