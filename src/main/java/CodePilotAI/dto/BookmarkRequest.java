package CodePilotAI.dto;

import jakarta.validation.constraints.NotNull;

public class BookmarkRequest {
    @NotNull(message = "questionId is required")
    private Long questionId;

    public Long getQuestionId() { return questionId; }
    public void setQuestionId(Long questionId) { this.questionId = questionId; }
}
