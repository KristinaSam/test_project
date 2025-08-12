package by.test.com.test_project.model.repository;

import by.test.com.test_project.model.entity.PatientProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PatientProfileRepository extends JpaRepository<PatientProfile, Long> {

    @Query("SELECT p FROM PatientProfile p WHERE p.statusId IN (200,210,230) AND p.oldClientGuid LIKE %:guid%")

   List<PatientProfile> findActiveByOldClientGuid(@Param("guid") String guid);

    Optional<PatientProfile> findByOldClientGuid(String oldClientGuid);
}


