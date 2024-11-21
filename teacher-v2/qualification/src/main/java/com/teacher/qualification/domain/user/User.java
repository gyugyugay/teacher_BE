package com.teacher.qualification.domain.user;

import com.teacher.qualification.domain.comment.Comment;
import com.teacher.qualification.domain.post.Post;
import com.teacher.qualification.domain.question.Question;
import com.teacher.qualification.dto.user.UserUpdateDto;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "`user`")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;
    private String nickname;
    private String password;
    private String phoneNumber;
    private String email;
    private String name;

    @Builder.Default
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Post> posts = new ArrayList<>();
    @Builder.Default
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Question> questions = new ArrayList<>();
    @Builder.Default
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Comment> comments = new ArrayList<>();
    @Builder.Default
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<AnswerHistory> answerHistories = new ArrayList<>();

    public void updatePassword(String tempPassword) {
        this.password = tempPassword;
    }

    public void update(UserUpdateDto userUpdateDto) {
        this.nickname = userUpdateDto.nickname();
        this.password = userUpdateDto.password();
        this.phoneNumber = userUpdateDto.phoneNumber();
        this.email = userUpdateDto.email();
        this.name = userUpdateDto.name();
    }
}
