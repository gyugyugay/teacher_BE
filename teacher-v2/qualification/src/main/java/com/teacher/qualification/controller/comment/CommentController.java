package com.teacher.qualification.controller.comment;

import com.teacher.qualification.dto.comment.CommentInfo;
import com.teacher.qualification.dto.comment.ModifyCommentDto;
import com.teacher.qualification.dto.comment.WriteCommentDto;
import com.teacher.qualification.service.comment.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comment")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @GetMapping("{postId}")
    @Operation(summary = "게시판 댓글 불러오기")
    public ResponseEntity<List<CommentInfo>> getAllComment(@PathVariable Long postId) {
        List<CommentInfo> comments = commentService.getAllComment(postId);

        return ResponseEntity.ok(comments);
    }

    @PostMapping("/{postId}")
    @Operation(summary = "게시판 댓글 작성하기")
    public ResponseEntity<String> createComment(@PathVariable Long postId, @RequestBody WriteCommentDto comment) {
        boolean isCommented = commentService.createComment(postId, comment);

        if (!isCommented) {
            return ResponseEntity.status(401).body("로그인을 하고 댓글을 작성해주세요");
        }

        return ResponseEntity.status(200).body("댓글이 성공적으로 작성되었습니다");
    }

    @PutMapping("/{commentId}")
    @Operation(summary = "게시판 댓글 수정하기")
    public ResponseEntity<String> updateComment(@PathVariable Long commentId, @RequestBody ModifyCommentDto commentDto){
        boolean completed = commentService.updateComment(commentId, commentDto);
        if (completed) {
            return ResponseEntity.status(200).body("댓글이 성공적으로 수정되었습니다.");
        }
        return ResponseEntity.status(401).body("댓글을 수정할 권한이 없습니다.");
    }

    @DeleteMapping("/{commentId}")
    @Operation(summary = "게시판 댓글 삭제하기")
    public ResponseEntity<String> deleteComment(@PathVariable Long commentId){
        boolean completed = commentService.deleteComment(commentId);
        if (completed) {
            return ResponseEntity.status(200).body("댓글이 성공적으로 삭제되었습니다.");
        }
        return ResponseEntity.status(401).body("댓글을 수정할 권한이 없습니다.");
    }
}

