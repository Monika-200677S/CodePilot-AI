package CodePilotAI.dto;

public class ProgressResponse {
    private Integer questionsSolved;
    private Integer questionsAttempted;
    private Double accuracy;
    private String weakTopic;
    private Integer easySolved;
    private Integer mediumSolved;
    private Integer hardSolved;
    private Integer currentStreak;
    private Integer longestStreak;
    private Integer contestRating;

    public ProgressResponse() {}

    public ProgressResponse(Integer questionsSolved, Integer questionsAttempted, Double accuracy, String weakTopic,
                             Integer easySolved, Integer mediumSolved, Integer hardSolved,
                             Integer currentStreak, Integer longestStreak, Integer contestRating) {
        this.questionsSolved = questionsSolved;
        this.questionsAttempted = questionsAttempted;
        this.accuracy = accuracy;
        this.weakTopic = weakTopic;
        this.easySolved = easySolved;
        this.mediumSolved = mediumSolved;
        this.hardSolved = hardSolved;
        this.currentStreak = currentStreak;
        this.longestStreak = longestStreak;
        this.contestRating = contestRating;
    }

    public Integer getQuestionsSolved() { return questionsSolved; }
    public void setQuestionsSolved(Integer questionsSolved) { this.questionsSolved = questionsSolved; }
    public Integer getQuestionsAttempted() { return questionsAttempted; }
    public void setQuestionsAttempted(Integer questionsAttempted) { this.questionsAttempted = questionsAttempted; }
    public Double getAccuracy() { return accuracy; }
    public void setAccuracy(Double accuracy) { this.accuracy = accuracy; }
    public String getWeakTopic() { return weakTopic; }
    public void setWeakTopic(String weakTopic) { this.weakTopic = weakTopic; }
    public Integer getEasySolved() { return easySolved; }
    public void setEasySolved(Integer easySolved) { this.easySolved = easySolved; }
    public Integer getMediumSolved() { return mediumSolved; }
    public void setMediumSolved(Integer mediumSolved) { this.mediumSolved = mediumSolved; }
    public Integer getHardSolved() { return hardSolved; }
    public void setHardSolved(Integer hardSolved) { this.hardSolved = hardSolved; }
    public Integer getCurrentStreak() { return currentStreak; }
    public void setCurrentStreak(Integer currentStreak) { this.currentStreak = currentStreak; }
    public Integer getLongestStreak() { return longestStreak; }
    public void setLongestStreak(Integer longestStreak) { this.longestStreak = longestStreak; }
    public Integer getContestRating() { return contestRating; }
    public void setContestRating(Integer contestRating) { this.contestRating = contestRating; }
}
