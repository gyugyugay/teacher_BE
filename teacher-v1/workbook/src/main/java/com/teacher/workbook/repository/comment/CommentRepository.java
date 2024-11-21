package com.teacher.workbook.repository.comment;

import com.teacher.workbook.domain.comment.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
