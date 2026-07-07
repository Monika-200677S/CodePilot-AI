package CodePilotAI.service;

import CodePilotAI.entity.Bookmark;
import CodePilotAI.entity.Question;
import CodePilotAI.entity.User;
import CodePilotAI.exception.ResourceNotFoundException;
import CodePilotAI.repository.BookmarkRepository;
import CodePilotAI.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BookmarkService {

    @Autowired
    private BookmarkRepository bookmarkRepository;

    @Autowired
    private QuestionRepository questionRepository;

    public List<Bookmark> getBookmarks(User user) {
        return bookmarkRepository.findByUser(user);
    }

    @Transactional
    public Bookmark addBookmark(User user, Long questionId) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new ResourceNotFoundException("Question not found with id: " + questionId));

        return bookmarkRepository.findByUserAndQuestionId(user, questionId)
                .orElseGet(() -> {
                    Bookmark bookmark = new Bookmark();
                    bookmark.setUser(user);
                    bookmark.setQuestion(question);
                    return bookmarkRepository.save(bookmark);
                });
    }

    @Transactional
    public void removeBookmark(User user, Long questionId) {
        bookmarkRepository.deleteByUserAndQuestionId(user, questionId);
    }

    @Transactional
    public Bookmark updateNotes(User user, Long questionId, String notes) {
        Bookmark bookmark = bookmarkRepository.findByUserAndQuestionId(user, questionId)
                .orElseGet(() -> addBookmark(user, questionId));
        bookmark.setNotes(notes);
        return bookmarkRepository.save(bookmark);
    }
}
