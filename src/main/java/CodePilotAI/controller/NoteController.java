package CodePilotAI.controller;

import CodePilotAI.dto.NotesRequest;
import CodePilotAI.entity.User;
import CodePilotAI.service.NoteService;
import CodePilotAI.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/notes")
public class NoteController {

    @Autowired
    private NoteService noteService;

    @Autowired
    private UserService userService;

    @GetMapping("/{questionId}")
    public Map<String, String> getNote(@AuthenticationPrincipal UserDetails principal, @PathVariable Long questionId) {
        User user = userService.getByEmail(principal.getUsername());
        return Map.of("content", noteService.getNote(user, questionId));
    }

    @PutMapping("/{questionId}")
    public Map<String, String> saveNote(@AuthenticationPrincipal UserDetails principal,
                                         @PathVariable Long questionId,
                                         @RequestBody NotesRequest request) {
        User user = userService.getByEmail(principal.getUsername());
        String saved = noteService.saveNote(user, questionId, request.getNotes());
        return Map.of("content", saved);
    }
}
