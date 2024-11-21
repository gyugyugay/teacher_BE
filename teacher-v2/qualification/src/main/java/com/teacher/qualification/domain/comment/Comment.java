package com.teacher.qualification.domain.comment;

import com.teacher.qualification.domain.post.Post;
import com.teacher.qualification.domain.user.User;
import com.teacher.qualification.dto.comment.WriteCommentDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@Entity
public class Comment {
    @Id
    @Column(name="comment_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @Lob
    private String comment;

    private LocalDateTime createdAt;

    public Comment(User user, Post post, WriteCommentDto commentDto) {
        this.user = user;
        this.post = post;
        this.comment = commentDto.comment();
    }

    public Comment() {

    }

    @PrePersist
    public void onPrePersist() {
        this.createdAt = LocalDateTime.now();
    }
}


