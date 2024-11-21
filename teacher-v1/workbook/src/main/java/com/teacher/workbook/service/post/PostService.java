package com.teacher.workbook.service.post;

import com.teacher.workbook.domain.post.Post;
import com.teacher.workbook.domain.user.User;
import com.teacher.workbook.dto.post.PostCreateDto;
import com.teacher.workbook.dto.post.PostDetailDto;
import com.teacher.workbook.dto.post.PostListDto;
import com.teacher.workbook.dto.post.PostUpdateRequestDto;
import com.teacher.workbook.repository.post.PostRepository;
import com.teacher.workbook.repository.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostService {
    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    // 게시글 반환
    public List<PostListDto> getAllPosts() {
        return postRepository.findAll().stream().map(post -> new PostListDto(
                post.getId(),
                post.getTitle(),
                post.getUser().getNickname(),
                post.getUser().getId(),
                post.getUpdatedAt()
        )).collect(Collectors.toList());
    }

    // 게시글 작성
    public Post createPost(PostCreateDto request) {
        User user = userRepository.findById(request.getUserId()).orElseThrow(
                () -> new RuntimeException("사용자를 찾을 수 없습니다.")
        );

        Post post = new Post();
        post.setTitle(request.getTitle());
        post.setContent(request.getContent());
        post.setUser(user);
        post.setCreatedAt(LocalDateTime.now());
        post.setUpdatedAt(LocalDateTime.now());

        return postRepository.save(post);
    }

    // 특정 게시물 조희
    public PostDetailDto getPosts(Long id) {
        return postRepository.findById(id).map(post -> new PostDetailDto(
                post.getUser().getId(),
                post.getUser().getNickname(),
                post.getTitle(),
                post.getContent(),
                post.getUpdatedAt()
        )).orElseThrow(() -> new IllegalArgumentException("해당 게시글을 찾을 수 없습니다. id=" + id));
    }

    // 게시글 수정
    public void updatePost(Long postId, Long userId, PostUpdateRequestDto requestDto) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다. postId=" + postId));

        if (!post.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("수정 권한이 없습니다. userId=" + userId);
        }
        post.setUpdatedAt(LocalDateTime.now());
        post.setTitle(requestDto.getTitle());
        post.setContent(requestDto.getContent());
        postRepository.save(post);
    }

    // 게시글 삭제
    public void deletePost(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다. id=" + id));
        postRepository.delete(post);
    }
}
