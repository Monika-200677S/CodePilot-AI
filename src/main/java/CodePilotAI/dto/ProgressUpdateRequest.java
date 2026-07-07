package CodePilotAI.dto;

public class ProgressUpdateRequest {
    private boolean solvedCorrectly;
    private String topic;
    private String difficulty; // "Easy" | "Medium" | "Hard" — optional but powers the difficulty breakdown

    public boolean isSolvedCorrectly() { return solvedCorrectly; }
    public void setSolvedCorrectly(boolean solvedCorrectly) { this.solvedCorrectly = solvedCorrectly; }
    public String getTopic() { return topic; }
    public void setTopic(String topic) { this.topic = topic; }
    public String getDifficulty() { return difficulty; }
    public void setDifficulty(String difficulty) { this.difficulty = difficulty; }
}
