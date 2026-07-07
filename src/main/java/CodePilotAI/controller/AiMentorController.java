package CodePilotAI.controller;

import CodePilotAI.dto.ChatRequest;
import CodePilotAI.dto.ChatResponse;
import CodePilotAI.dto.HintRequest;
import CodePilotAI.entity.Question;
import CodePilotAI.service.AiMentorService;
import CodePilotAI.service.QuestionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/mentor")
public class AiMentorController {

    @Autowired
    private AiMentorService aiMentorService;

    @Autowired
    private QuestionService questionService;

    @PostMapping("/ask")
    public ChatResponse ask(@Valid @RequestBody ChatRequest request) {
        String reply = aiMentorService.askMentor(request.getMessage());
        return new ChatResponse(reply);
    }

    // Progressive hints (level 1-3) instead of a full solution.
    @PostMapping("/hint")
    public ChatResponse getHint(@Valid @RequestBody HintRequest request) {
        Question question = questionService.getById(request.getQuestionId());
        String reply = aiMentorService.getHint(question, request.getHintLevel());
        return new ChatResponse(reply);
    }
}
