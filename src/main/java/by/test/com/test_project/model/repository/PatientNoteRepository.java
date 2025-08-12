package by.test.com.test_project.model.repository;

import by.test.com.test_project.model.entity.PatientNote;
import by.test.com.test_project.model.entity.PatientProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PatientNoteRepository extends JpaRepository<PatientNote, Integer> {

    Optional<PatientNote> findByPatientProfile_OldClientGuidContaining(String guid);
    List<PatientNote> findByPatientProfile(PatientProfile patientProfile);
    @Query("SELECT p FROM PatientNote p WHERE p.patientProfile.oldClientGuid = :oldClientGuid")
    List<PatientNote> findByPatientProfileOldClientGuid(@Param("oldClientGuid") String oldClientGuid);

}
