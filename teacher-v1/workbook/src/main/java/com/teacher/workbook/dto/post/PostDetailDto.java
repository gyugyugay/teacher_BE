package com.teacher.workbook.dto.post;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class PostDetailDto {
    private Long userId;
    private String nickname;
    private String title;
    private String content;
    private LocalDateTime lastModifiedAt;

    public PostDetailDto(Long userId, String nickname, String title, String content, LocalDateTime updatedAt) {
        this.userId = userId;
        this.nickname = nickname;
        this.title = title;
        this.content = content;
        this.lastModifiedAt = updatedAt;
    }
}
