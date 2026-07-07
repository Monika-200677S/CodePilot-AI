package CodePilotAI.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "questions")
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Lob
    @Column(length = 4000)
    private String description;

    @Lob
    @Column(length = 4000)
    private String examples;

    @Lob
    @Column(length = 2000)
    private String constraints;

    private String topic;

    private String difficulty;

    private String link;

    private String companies; // comma-separated: "Amazon,Google,Meta"

    // Getters
    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getExamples() {
        return examples;
    }

    public String getConstraints() {
        return constraints;
    }

    public String getTopic() {
        return topic;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public String getLink() {
        return link;
    }

    public String getCompanies() {
        return companies;
    }

    // Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setExamples(String examples) {
        this.examples = examples;
    }

    public void setConstraints(String constraints) {
        this.constraints = constraints;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public void setCompanies(String companies) {
        this.companies = companies;
    }
}
