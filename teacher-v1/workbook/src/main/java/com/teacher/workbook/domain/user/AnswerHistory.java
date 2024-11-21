package com.teacher.workbook.domain.user;

import com.teacher.workbook.domain.question.Question;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@Entity
public class AnswerHistory {
    @Id
    @Column(name = "history_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne
    @JoinColumn(name = "question_id")
    private Question question;
    @ElementCollection
    private Set<Integer> answers; // 선택한 Answer의 number들이 저장될거야
    private String subjectiveAnswer;  // 주관식 답변
    private boolean isCorrect;
    private LocalDateTime answeredAt;

    @PrePersist
    protected void onPrePersist() {
        answeredAt = LocalDateTime.now();
    }

}
