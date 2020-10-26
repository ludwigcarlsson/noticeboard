package se.experis.noticeboard.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import se.experis.noticeboard.models.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
