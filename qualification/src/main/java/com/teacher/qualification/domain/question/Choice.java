package com.teacher.qualification.domain.question;

import com.teacher.qualification.dto.question.request.OptionRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Choice {
    @Id
    @Column(name = "option_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "question_id")
    private Question question;
    private Integer number; // 몇 번 옵션인지
    @Lob
    private String content; // 옵션 내용

    public Choice(Question question, OptionRequestDto optionDto) {
        this.question = question;
        this.number = optionDto.number();
        this.content = optionDto.content();
    }

    public Choice() {

    }

}
