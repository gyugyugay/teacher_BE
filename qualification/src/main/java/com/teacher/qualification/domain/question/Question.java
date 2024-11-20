package com.teacher.qualification.domain.question;

import com.teacher.qualification.domain.user.AnswerHistory;
import com.teacher.qualification.domain.user.User;
import com.teacher.qualification.dto.question.request.QuestionRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
public class Question {
    @Id
    @Column(name = "question_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Lob
    private String title; // 문제 게시글 제목
    @Lob
    private String content; // 문제 내용
    private QuestionType questionType; // 4 또는 5
    private String image; // 해설 이미지 경로 저장
    private Boolean isPastExam;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Choice> choices; // 선택지들

    @OneToOne(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    private Answer answer; // 답 저장

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<AnswerHistory> answerHistories; // AnswerHistory와의 관계 설정

    private Integer totalPeopleNum; // 사용자들이 문제를 시도한 수
    private Integer totalCorrectPeopleNum; // 사용자들이 문제를 맞춘 수

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Question(User user, QuestionRequestDto questionDto) {
        this.user = user;
        this.title = questionDto.title();
        this.content = questionDto.content();
        this.questionType = questionDto.questionType();
        this.image = questionDto.image();
        this.isPastExam = questionDto.isPastExam();
    }

    public Question() {}

    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        createdAt = now;
        updatedAt = now;
        totalPeopleNum = 0; // 문제를 시도한 사용자 수 초기화
        totalCorrectPeopleNum = 0; // 문제를 맞춘 사용자 수 초기화
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public void update(QuestionRequestDto questionRequestDto) {
        this.title = questionRequestDto.title();
        this.content = questionRequestDto.content();
        this.questionType = questionRequestDto.questionType();
        this.image = questionRequestDto.image();
        this.isPastExam = questionRequestDto.isPastExam();
    }
}
