package CodePilotAI.repository;

import CodePilotAI.entity.StudentProgress;
import CodePilotAI.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudentProgressRepository extends JpaRepository<StudentProgress, Long> {
    Optional<StudentProgress> findByUser(User user);
}
