package com.teacher.qualification.repository.post;

import com.teacher.qualification.domain.post.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
