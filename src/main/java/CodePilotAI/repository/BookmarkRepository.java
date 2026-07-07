package CodePilotAI.repository;

import CodePilotAI.entity.Bookmark;
import CodePilotAI.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {
    List<Bookmark> findByUser(User user);
    Optional<Bookmark> findByUserAndQuestionId(User user, Long questionId);
    void deleteByUserAndQuestionId(User user, Long questionId);
}
