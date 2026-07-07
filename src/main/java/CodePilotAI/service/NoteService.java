package CodePilotAI.service;

import CodePilotAI.entity.Note;
import CodePilotAI.entity.Question;
import CodePilotAI.entity.User;
import CodePilotAI.repository.NoteRepository;
import CodePilotAI.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class NoteService {

    @Autowired
    private NoteRepository noteRepository;

    @Autowired
    private QuestionRepository questionRepository;

    public String getNote(User user, Long questionId) {
        return noteRepository.findByUserAndQuestionId(user, questionId)
                .map(Note::getContent)
                .orElse("");
    }

    @Transactional
    public String saveNote(User user, Long questionId, String content) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new RuntimeException("Question not found"));

        Note note = noteRepository.findByUserAndQuestionId(user, questionId)
                .orElseGet(() -> {
                    Note n = new Note();
                    n.setUser(user);
                    n.setQuestion(question);
                    return n;
                });
        note.setContent(content);
        note.setUpdatedAt(LocalDateTime.now());
        noteRepository.save(note);
        return note.getContent();
    }
}
