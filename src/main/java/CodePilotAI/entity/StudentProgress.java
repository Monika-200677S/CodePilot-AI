package CodePilotAI.entity;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "student_progress")
public class StudentProgress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(nullable = false)
    private Integer questionsSolved = 0;

    @Column(nullable = false)
    private Integer questionsAttempted = 0;

    @Column(nullable = false)
    private Double accuracy = 0.0;

    private String weakTopic;

    @Column(nullable = false)
    private Integer easySolved = 0;

    @Column(nullable = false)
    private Integer mediumSolved = 0;

    @Column(nullable = false)
    private Integer hardSolved = 0;

    @Column(nullable = false)
    private Integer currentStreak = 0;

    @Column(nullable = false)
    private Integer longestStreak = 0;

    private LocalDate lastActivityDate;

    @Column(nullable = false)
    private Integer contestRating = 1200; // starts at a baseline like Codeforces/LeetCode-style ratings

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
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
    public LocalDate getLastActivityDate() { return lastActivityDate; }
    public void setLastActivityDate(LocalDate lastActivityDate) { this.lastActivityDate = lastActivityDate; }
    public Integer getContestRating() { return contestRating; }
    public void setContestRating(Integer contestRating) { this.contestRating = contestRating; }
}
