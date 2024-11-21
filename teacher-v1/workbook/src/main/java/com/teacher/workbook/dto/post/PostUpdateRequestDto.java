package com.teacher.workbook.dto.post;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostUpdateRequestDto {
    private String title;
    private String content;

    // 기본 생성자 및 getter, setter 생략

    public PostUpdateRequestDto(String title, String content) {
        this.title = title;
        this.content = content;
    }

    // getter 및 setter 생략
}