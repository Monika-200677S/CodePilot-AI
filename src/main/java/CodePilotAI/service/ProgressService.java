package CodePilotAI.service;

import CodePilotAI.dto.ProgressResponse;
import CodePilotAI.entity.ActivityLog;
import CodePilotAI.entity.StudentProgress;
import CodePilotAI.entity.User;
import CodePilotAI.repository.ActivityLogRepository;
import CodePilotAI.repository.StudentProgressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class ProgressService {

    @Autowired
    private StudentProgressRepository progressRepository;

    @Autowired
    private ActivityLogRepository activityLogRepository;

    public ProgressResponse getProgress(User user) {
        StudentProgress progress = progressRepository.findByUser(user)
                .orElseGet(() -> {
                    StudentProgress p = new StudentProgress();
                    p.setUser(user);
                    return p;
                });
        return toResponse(progress);
    }

    public ProgressResponse updateProgress(User user, boolean solvedCorrectly, String topic, String difficulty) {
        StudentProgress progress = progressRepository.findByUser(user)
                .orElseGet(() -> {
                    StudentProgress p = new StudentProgress();
                    p.setUser(user);
                    return p;
                });

        progress.setQuestionsAttempted(progress.getQuestionsAttempted() + 1);

        if (solvedCorrectly) {
            progress.setQuestionsSolved(progress.getQuestionsSolved() + 1);

            if (difficulty != null) {
                String d = difficulty.toLowerCase();
                int ratingDelta = 5;
                if (d.contains("easy")) {
                    progress.setEasySolved(progress.getEasySolved() + 1);
                    ratingDelta = 5;
                } else if (d.contains("hard")) {
                    progress.setHardSolved(progress.getHardSolved() + 1);
                    ratingDelta = 25;
                } else {
                    progress.setMediumSolved(progress.getMediumSolved() + 1);
                    ratingDelta = 12;
                }
                progress.setContestRating(progress.getContestRating() + ratingDelta);
            }

            recordActivityAndStreak(user, progress);
        } else if (topic != null && !topic.isBlank()) {
            progress.setWeakTopic(topic);
        }

        double accuracy = progress.getQuestionsAttempted() == 0 ? 0.0 :
                (progress.getQuestionsSolved() * 100.0) / progress.getQuestionsAttempted();
        progress.setAccuracy(Math.round(accuracy * 100.0) / 100.0);

        StudentProgress saved = progressRepository.save(progress);
        return toResponse(saved);
    }

    private void recordActivityAndStreak(User user, StudentProgress progress) {
        LocalDate today = LocalDate.now();

        ActivityLog log = activityLogRepository.findByUserAndActivityDate(user, today)
                .orElseGet(() -> {
                    ActivityLog a = new ActivityLog();
                    a.setUser(user);
                    a.setActivityDate(today);
                    a.setCount(0);
                    return a;
                });
        log.setCount(log.getCount() + 1);
        activityLogRepository.save(log);

        LocalDate lastActivity = progress.getLastActivityDate();
        if (lastActivity == null) {
            progress.setCurrentStreak(1);
        } else if (lastActivity.equals(today)) {
            // already counted today, streak unchanged
        } else if (lastActivity.equals(today.minusDays(1))) {
            progress.setCurrentStreak(progress.getCurrentStreak() + 1);
        } else {
            progress.setCurrentStreak(1);
        }
        progress.setLastActivityDate(today);

        if (progress.getCurrentStreak() > progress.getLongestStreak()) {
            progress.setLongestStreak(progress.getCurrentStreak());
        }
    }

    // Returns the last `days` days as a map of ISO date -> activity count, for the heatmap.
    public Map<String, Integer> getHeatmap(User user, int days) {
        LocalDate end = LocalDate.now();
        LocalDate start = end.minusDays(days - 1L);

        List<ActivityLog> logs = activityLogRepository
                .findByUserAndActivityDateBetweenOrderByActivityDateAsc(user, start, end);

        Map<String, Integer> byDate = new LinkedHashMap<>();
        for (LocalDate d = start; !d.isAfter(end); d = d.plusDays(1)) {
            byDate.put(d.toString(), 0);
        }
        for (ActivityLog log : logs) {
            byDate.put(log.getActivityDate().toString(), log.getCount());
        }
        return byDate;
    }

    private ProgressResponse toResponse(StudentProgress p) {
        return new ProgressResponse(
                p.getQuestionsSolved(), p.getQuestionsAttempted(), p.getAccuracy(), p.getWeakTopic(),
                p.getEasySolved(), p.getMediumSolved(), p.getHardSolved(),
                p.getCurrentStreak(), p.getLongestStreak(), p.getContestRating()
        );
    }
}
