package com.teacher.workbook.repository.post;

import com.teacher.workbook.domain.post.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
