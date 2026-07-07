package CodePilotAI.controller;

import CodePilotAI.entity.Question;
import CodePilotAI.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/questions")
@CrossOrigin("*")
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @PostMapping("/add")
    @PreAuthorize("hasRole('ADMIN')")
    public Question addQuestion(@RequestBody Question question) {
        return questionService.addQuestion(question);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Question updateQuestion(@PathVariable Long id, @RequestBody Question question) {
        return questionService.updateQuestion(id, question);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteQuestion(@PathVariable Long id) {
        questionService.deleteQuestion(id);
    }

    @GetMapping("/all")
    public List<Question> getAllQuestions() {
        return questionService.getAllQuestions();
    }

    @GetMapping("/topic/{topic}")
    public List<Question> getByTopic(@PathVariable String topic) {
        return questionService.getByTopic(topic);
    }

    @GetMapping("/difficulty/{difficulty}")
    public List<Question> getByDifficulty(@PathVariable String difficulty) {
        return questionService.getByDifficulty(difficulty);
    }

    @GetMapping("/{id}")
    public Question getById(@PathVariable Long id) {
        return questionService.getById(id);
    }
}
