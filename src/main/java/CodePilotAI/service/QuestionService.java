package CodePilotAI.service;

import CodePilotAI.entity.Question;
import CodePilotAI.exception.ResourceNotFoundException;
import CodePilotAI.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuestionService {

    @Autowired
    private QuestionRepository questionRepository;

    public Question addQuestion(Question question) {
        return questionRepository.save(question);
    }

    public List<Question> getAllQuestions() {
        return questionRepository.findAll();
    }

    public List<Question> getByTopic(String topic) {
        return questionRepository.findByTopicIgnoreCase(topic);
    }

    public List<Question> getByDifficulty(String difficulty) {
        return questionRepository.findByDifficultyIgnoreCase(difficulty);
    }

    public Question getById(Long id) {
        return questionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Question not found with id: " + id));
    }

    public Question updateQuestion(Long id, Question updated) {
        Question existing = getById(id);
        existing.setTitle(updated.getTitle());
        existing.setDescription(updated.getDescription());
        existing.setExamples(updated.getExamples());
        existing.setConstraints(updated.getConstraints());
        existing.setTopic(updated.getTopic());
        existing.setDifficulty(updated.getDifficulty());
        existing.setLink(updated.getLink());
        existing.setCompanies(updated.getCompanies());
        return questionRepository.save(existing);
    }

    public void deleteQuestion(Long id) {
        Question existing = getById(id);
        questionRepository.delete(existing);
    }
}
