package CodePilotAI.service;

import CodePilotAI.dto.ResumeAnalysisResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class ResumeService {

    @Autowired
    private AiMentorService aiMentorService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public ResumeAnalysisResponse analyze(MultipartFile file) throws IOException {
        String text = extractText(file);

        if (text.isBlank()) {
            ResumeAnalysisResponse fallback = new ResumeAnalysisResponse();
            fallback.setAtsScore(0);
            fallback.setWeakPoints(List.of("Could not extract any text from this file."));
            fallback.setMissingKeywords(List.of());
            fallback.setSkills(List.of());
            return fallback;
        }

        // Trim very long resumes so the prompt stays reasonable.
        if (text.length() > 8000) {
            text = text.substring(0, 8000);
        }

        String prompt = "You are an ATS (Applicant Tracking System) and resume reviewer for software " +
                "engineering / coding-interview job applications. Analyze the resume text below and " +
                "respond with STRICT JSON ONLY (no markdown, no code fences, no commentary) matching " +
                "exactly this shape:\n" +
                "{\"atsScore\": <integer 0-100>, \"weakPoints\": [<string>, ...], " +
                "\"missingKeywords\": [<string>, ...], \"skills\": [<string>, ...]}\n\n" +
                "atsScore reflects how well this resume would pass automated ATS screening for a " +
                "software engineering role. weakPoints are concrete, actionable critiques. " +
                "missingKeywords are common technical/keyword terms recruiters search for that this " +
                "resume is missing. skills are the technical skills you can clearly find already on " +
                "the resume.\n\nResume text:\n" + text;

        String aiReply = aiMentorService.askMentor(prompt);
        return parseAiResponse(aiReply);
    }

    private String extractText(MultipartFile file) throws IOException {
        String filename = file.getOriginalFilename() != null ? file.getOriginalFilename().toLowerCase() : "";

        if (filename.endsWith(".pdf")) {
            try (PDDocument document = PDDocument.load(file.getInputStream())) {
                return new PDFTextStripper().getText(document);
            }
        }

        // Treat anything else (.txt, etc.) as plain text.
        return new String(file.getBytes());
    }

    private ResumeAnalysisResponse parseAiResponse(String aiReply) {
        ResumeAnalysisResponse response = new ResumeAnalysisResponse();
        try {
            String cleaned = aiReply.trim();
            // Strip markdown code fences if the model added them despite instructions.
            if (cleaned.startsWith("```")) {
                cleaned = cleaned.replaceAll("^```[a-zA-Z]*", "").replaceAll("```$", "").trim();
            }
            var node = objectMapper.readTree(cleaned);
            response.setAtsScore(node.path("atsScore").asInt(0));
            response.setWeakPoints(toStringList(node.path("weakPoints")));
            response.setMissingKeywords(toStringList(node.path("missingKeywords")));
            response.setSkills(toStringList(node.path("skills")));
        } catch (Exception e) {
            // The model didn't return valid JSON — fall back to showing the raw text
            // rather than failing the request outright.
            response.setAtsScore(0);
            response.setWeakPoints(List.of());
            response.setMissingKeywords(List.of());
            response.setSkills(List.of());
            response.setRawFeedback(aiReply);
        }
        return response;
    }

    private List<String> toStringList(com.fasterxml.jackson.databind.JsonNode arrayNode) {
        List<String> result = new java.util.ArrayList<>();
        if (arrayNode.isArray()) {
            arrayNode.forEach(n -> result.add(n.asText()));
        }
        return result;
    }
}
