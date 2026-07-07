package CodePilotAI.service;

import CodePilotAI.entity.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;

@Service
public class AiMentorService {

    @Autowired
    private WebClient webClient;

    @Value("${ai.provider}")
    private String provider;

    @Value("${gemini.api.key}")
    private String geminiApiKey;

    @Value("${gemini.api.url}")
    private String geminiApiUrl;

    @Value("${openai.api.key}")
    private String openaiApiKey;

    @Value("${openai.api.url}")
    private String openaiApiUrl;

    private static final String SYSTEM_PROMPT =
            "You are CodeMentor AI, a friendly and knowledgeable mentor helping students prepare for " +
            "coding interviews. You explain data structures and algorithms clearly, suggest practice " +
            "questions by topic (Arrays, Graphs, Trees, DP, Strings, etc.), and help debug student code. " +
            "Keep answers concise, use examples, and encourage the student.";


    public String getHint(Question question, int hintLevel) {
        String levelInstruction;
        if (hintLevel <= 1) {
            levelInstruction = "Give ONLY a very high-level nudge about which approach or data structure to consider. Do not mention specific steps.";
        } else if (hintLevel == 2) {
            levelInstruction = "Give a more specific hint about the approach or algorithm, including key steps, but do NOT write any code.";
        } else {
            levelInstruction = "Give a detailed hint including pseudocode or the key logic, but still do NOT provide the complete, final solution code.";
        }

        String prompt = "A student is working on this coding interview problem:\n\n" +
                "Title: " + question.getTitle() + "\n" +
                "Topic: " + question.getTopic() + " | Difficulty: " + question.getDifficulty() + "\n" +
                "Description: " + question.getDescription() + "\n\n" +
                "Give hint level " + hintLevel + " of 3. " + levelInstruction +
                " Never reveal the complete solution. Keep it under 100 words.";

        return askMentor(prompt);
    }

    public String askMentor(String userMessage) {
        try {
            if ("openai".equalsIgnoreCase(provider)) {
                return callOpenAi(userMessage);
            }
            return callGemini(userMessage);
        } catch (Exception e) {
            return "Sorry, the AI mentor is temporarily unavailable. Please check that your API key is " +
                   "configured correctly in application.properties. (" + e.getMessage() + ")";
        }
    }

    @SuppressWarnings("unchecked")
    private String callGemini(String userMessage) {
        String url = geminiApiUrl + "?key=" + geminiApiKey;

        Map<String, Object> body = Map.of(
                "contents", List.of(
                        Map.of("role", "user", "parts", List.of(
                                Map.of("text", SYSTEM_PROMPT + "\n\nStudent question: " + userMessage)
                        ))
                )
        );

        Map response = webClient.post()
                .uri(url)
                .header("Content-Type", "application/json")
                .bodyValue(body)
                .retrieve()
                .bodyToMono(Map.class)
                .block();

        try {
            List<Map> candidates = (List<Map>) response.get("candidates");
            Map content = (Map) candidates.get(0).get("content");
            List<Map> parts = (List<Map>) content.get("parts");
            return (String) parts.get(0).get("text");
        } catch (Exception e) {
            return "The AI mentor returned an unexpected response format.";
        }
    }

    @SuppressWarnings("unchecked")
    private String callOpenAi(String userMessage) {
        Map<String, Object> body = Map.of(
                "model", "gpt-4o-mini",
                "messages", List.of(
                        Map.of("role", "system", "content", SYSTEM_PROMPT),
                        Map.of("role", "user", "content", userMessage)
                )
        );

        Map response = webClient.post()
                .uri(openaiApiUrl)
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + openaiApiKey)
                .bodyValue(body)
                .retrieve()
                .bodyToMono(Map.class)
                .block();

        try {
            List<Map> choices = (List<Map>) response.get("choices");
            Map message = (Map) choices.get(0).get("message");
            return (String) message.get("content");
        } catch (Exception e) {
            return "The AI mentor returned an unexpected response format.";
        }
    }
}
