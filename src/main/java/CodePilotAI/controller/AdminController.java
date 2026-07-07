package CodePilotAI.controller;

import CodePilotAI.dto.AdminUserView;
import CodePilotAI.entity.User;
import CodePilotAI.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    @Autowired
    private UserService userService;

    @GetMapping("/users")
    public List<AdminUserView> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return users.stream()
                .map(u -> new AdminUserView(u.getId(), u.getName(), u.getEmail(), u.getRole(), u.getCreatedAt()))
                .toList();
    }
}
