package aula.iceibank.repository;

import aula.iceibank.entity.EventLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EventLogRepository extends JpaRepository<EventLog, UUID> {
    List<EventLog> findAllByOrderByLamportTimestampAscAgencyIdAscWallClockAsc();
    Optional<EventLog> findTopByAgencyIdOrderByLamportTimestampDesc(Integer agencyId);
}
