package com.teacher.qualification.dto.post;

import java.time.LocalDateTime;

public record PostDetailDto(
        Long userId,
        String nickname,
        String title,
        String content,
        LocalDateTime lastModifiedAt
) {
}