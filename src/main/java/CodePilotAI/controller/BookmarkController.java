package CodePilotAI.controller;

import CodePilotAI.dto.BookmarkRequest;
import CodePilotAI.dto.NotesRequest;
import CodePilotAI.entity.Bookmark;
import CodePilotAI.entity.User;
import CodePilotAI.service.BookmarkService;
import CodePilotAI.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookmarks")
public class BookmarkController {

    @Autowired
    private BookmarkService bookmarkService;

    @Autowired
    private UserService userService;

    @GetMapping
    public List<Bookmark> getBookmarks(@AuthenticationPrincipal UserDetails principal) {
        User user = userService.getByEmail(principal.getUsername());
        return bookmarkService.getBookmarks(user);
    }

    @PostMapping
    public Bookmark addBookmark(@AuthenticationPrincipal UserDetails principal,
                                 @Valid @RequestBody BookmarkRequest request) {
        User user = userService.getByEmail(principal.getUsername());
        return bookmarkService.addBookmark(user, request.getQuestionId());
    }

    @DeleteMapping("/{questionId}")
    public void removeBookmark(@AuthenticationPrincipal UserDetails principal,
                                @PathVariable Long questionId) {
        User user = userService.getByEmail(principal.getUsername());
        bookmarkService.removeBookmark(user, questionId);
    }

    @PutMapping("/{questionId}/notes")
    public Bookmark updateNotes(@AuthenticationPrincipal UserDetails principal,
                                 @PathVariable Long questionId,
                                 @RequestBody NotesRequest request) {
        User user = userService.getByEmail(principal.getUsername());
        return bookmarkService.updateNotes(user, questionId, request.getNotes());
    }
}
