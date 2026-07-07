package CodePilotAI.dto;

import jakarta.validation.constraints.NotNull;

public class HintRequest {
    @NotNull
    private Long questionId;

    private int hintLevel = 1; // 1, 2, or 3 — higher levels give more specific hints

    public Long getQuestionId() { return questionId; }
    public void setQuestionId(Long questionId) { this.questionId = questionId; }
    public int getHintLevel() { return hintLevel; }
    public void setHintLevel(int hintLevel) { this.hintLevel = hintLevel; }
}
