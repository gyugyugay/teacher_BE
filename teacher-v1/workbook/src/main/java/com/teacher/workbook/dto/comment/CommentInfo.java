package com.teacher.workbook.dto.comment;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CommentInfo {
    private Long comment_id;
    private String content;
    private String author;
    private LocalDateTime create_time;
}
