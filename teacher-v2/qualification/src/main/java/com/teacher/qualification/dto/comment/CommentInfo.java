package com.teacher.qualification.dto.comment;

import java.time.LocalDateTime;

public record CommentInfo(
        Long author_id,
        Long comment_id,
        String content,
        String author,
        LocalDateTime create_time
) {

}