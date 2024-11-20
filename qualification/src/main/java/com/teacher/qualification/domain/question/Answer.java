package com.teacher.qualification.domain.question;

import com.teacher.qualification.dto.question.request.AnswerRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Answer {
    @Id
    @Column(name = "answer_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "question_id")
    private Question question;

    private String answers; // 정답. 복수 선택 가능하므로 "1,2"와 같은 형태로 저장
    @Lob
    private String subjectiveAnswer; // 주관식 정답
    private String image; // 해설 이미지
    @Lob
    @Column(name = "commentary", columnDefinition = "MEDIUMTEXT")
    private String commentary; // 해설

    public Answer(Question question, AnswerRequestDto answerDto) {
        this.question = question;
        this.answers = answerDto.answers();
        this.subjectiveAnswer = answerDto.subjectiveAnswer();
        this.image = answerDto.image();
        this.commentary = answerDto.commentary();
    }

    public Answer() {

    }
}


