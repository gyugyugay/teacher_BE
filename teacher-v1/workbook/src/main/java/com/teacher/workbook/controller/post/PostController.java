package com.teacher.workbook.controller.post;

import com.teacher.workbook.domain.post.Post;
import com.teacher.workbook.dto.post.PostCreateDto;
import com.teacher.workbook.dto.post.PostDetailDto;
import com.teacher.workbook.dto.post.PostUpdateRequestDto;
import com.teacher.workbook.repository.post.PostRepository;
import com.teacher.workbook.dto.post.PostListDto;
import com.teacher.workbook.service.post.PostService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {
    @Autowired
    private PostRepository postRepository;

    @Autowired
    private PostService postService;

    @GetMapping
    @Operation(summary = "모든 게시글 조회")
    public ResponseEntity<List<PostListDto>> getAllPosts() {
        List<PostListDto> posts = postService.getAllPosts();
        return ResponseEntity.ok(posts);
    }

    @PostMapping
    @Operation(summary = "게시글 생성")
    public ResponseEntity<Post> createPost(@RequestBody PostCreateDto request) {
        Post savedPost = postService.createPost(request);
        return ResponseEntity.ok(savedPost);
    }

    @GetMapping("/{id}")
    @Operation(summary = "특정 게시글 조회")
    public ResponseEntity<PostDetailDto> getPosts(@PathVariable Long id) {
        PostDetailDto posts = postService.getPosts(id);
        return ResponseEntity.ok(posts);
    }

    @PutMapping("/{post_id}/{user_id}")
    @Operation(summary = "특정 게시글 수정")
    public ResponseEntity<Void> updatePost(@PathVariable Long post_id, @PathVariable Long user_id, @RequestBody PostUpdateRequestDto requestDto) {
        postService.updatePost(post_id, user_id, requestDto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "특정 게시물 삭제")
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
        postService.deletePost(id);
        return ResponseEntity.ok().build();
    }
}
