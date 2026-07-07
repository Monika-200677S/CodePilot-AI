package CodePilotAI.repository;

import CodePilotAI.entity.Note;
import CodePilotAI.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NoteRepository extends JpaRepository<Note, Long> {
    Optional<Note> findByUserAndQuestionId(User user, Long questionId);
}
