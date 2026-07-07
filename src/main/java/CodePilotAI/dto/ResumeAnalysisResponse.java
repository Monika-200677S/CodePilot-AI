package CodePilotAI.dto;

import java.util.List;

public class ResumeAnalysisResponse {
    private int atsScore;
    private List<String> weakPoints;
    private List<String> missingKeywords;
    private List<String> skills;
    private String rawFeedback; // populated only if structured parsing failed, as a fallback

    public int getAtsScore() { return atsScore; }
    public void setAtsScore(int atsScore) { this.atsScore = atsScore; }
    public List<String> getWeakPoints() { return weakPoints; }
    public void setWeakPoints(List<String> weakPoints) { this.weakPoints = weakPoints; }
    public List<String> getMissingKeywords() { return missingKeywords; }
    public void setMissingKeywords(List<String> missingKeywords) { this.missingKeywords = missingKeywords; }
    public List<String> getSkills() { return skills; }
    public void setSkills(List<String> skills) { this.skills = skills; }
    public String getRawFeedback() { return rawFeedback; }
    public void setRawFeedback(String rawFeedback) { this.rawFeedback = rawFeedback; }
}
