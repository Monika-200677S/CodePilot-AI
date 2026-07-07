package CodePilotAI.repository;

import CodePilotAI.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    List<Question> findByTopicIgnoreCase(String topic);
    List<Question> findByDifficultyIgnoreCase(String difficulty);
}
