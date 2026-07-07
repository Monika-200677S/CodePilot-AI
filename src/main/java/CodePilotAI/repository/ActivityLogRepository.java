package CodePilotAI.repository;

import CodePilotAI.entity.ActivityLog;
import CodePilotAI.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ActivityLogRepository extends JpaRepository<ActivityLog, Long> {
    Optional<ActivityLog> findByUserAndActivityDate(User user, LocalDate date);
    List<ActivityLog> findByUserAndActivityDateBetweenOrderByActivityDateAsc(User user, LocalDate start, LocalDate end);
}
