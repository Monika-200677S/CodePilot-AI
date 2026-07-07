package CodePilotAI.controller;

import CodePilotAI.dto.ProgressResponse;
import CodePilotAI.dto.ProgressUpdateRequest;
import CodePilotAI.entity.User;
import CodePilotAI.service.ProgressService;
import CodePilotAI.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/progress")
public class ProgressController {

    @Autowired
    private ProgressService progressService;

    @Autowired
    private UserService userService;

    @GetMapping
    public ProgressResponse getProgress(@AuthenticationPrincipal UserDetails principal) {
        User user = userService.getByEmail(principal.getUsername());
        return progressService.getProgress(user);
    }

    @PostMapping("/update")
    public ProgressResponse updateProgress(@AuthenticationPrincipal UserDetails principal,
                                            @RequestBody ProgressUpdateRequest request) {
        User user = userService.getByEmail(principal.getUsername());
        return progressService.updateProgress(user, request.isSolvedCorrectly(), request.getTopic(), request.getDifficulty());
    }

    // Returns the last `days` days of activity as { "2026-07-01": 2, ... } for the heatmap.
    @GetMapping("/heatmap")
    public Map<String, Integer> getHeatmap(@AuthenticationPrincipal UserDetails principal,
                                            @RequestParam(defaultValue = "119") int days) {
        User user = userService.getByEmail(principal.getUsername());
        return progressService.getHeatmap(user, days);
    }
}
