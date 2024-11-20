package com.teacher.qualification.repository.comment;

import com.teacher.qualification.domain.comment.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
