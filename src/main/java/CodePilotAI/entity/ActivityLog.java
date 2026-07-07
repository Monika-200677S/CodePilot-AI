package CodePilotAI.entity;

import jakarta.persistence.*;

import java.time.LocalDate;

// One row per user per day they solved/attempted something.
// Powers the GitHub-style heatmap and streak calculations.
@Entity
@Table(name = "activity_log", uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "activity_date"}))
public class ActivityLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "activity_date", nullable = false)
    private LocalDate activityDate;

    @Column(nullable = false)
    private Integer count = 0;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public LocalDate getActivityDate() { return activityDate; }
    public void setActivityDate(LocalDate activityDate) { this.activityDate = activityDate; }
    public Integer getCount() { return count; }
    public void setCount(Integer count) { this.count = count; }
}
