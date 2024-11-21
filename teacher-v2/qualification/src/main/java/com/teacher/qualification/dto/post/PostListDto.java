package com.teacher.qualification.dto.post;

import java.time.LocalDateTime;

public record PostListDto(
        Long postId,
        String title,
        String nickname,
        Long userId,
        LocalDateTime updatedAt
) {
}
