package com.teacher.qualification.service.post;

import com.teacher.qualification.domain.post.Post;
import com.teacher.qualification.domain.user.User;
import com.teacher.qualification.dto.post.PostCreateDto;
import com.teacher.qualification.dto.post.PostDetailDto;
import com.teacher.qualification.dto.post.PostListDto;
import com.teacher.qualification.dto.post.PostUpdateRequestDto;
import com.teacher.qualification.repository.post.PostRepository;
import com.teacher.qualification.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostService {
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserService userService;

    // 게시글 반환
    @Transactional(readOnly = true)
    public List<PostListDto> getAllPosts() {
        return postRepository.findAll().stream()
                .filter(post -> post.getUser() != null)  // User가 null인 게시글은 제외
                .map(post -> new PostListDto(
                        post.getId(),
                        post.getTitle(),
                        post.getUser().getNickname(),
                        post.getUser().getId(),
                        post.getUpdatedAt()
                ))
                .collect(Collectors.toList());
    }

    // 게시글 작성
    @Transactional
    public void createPost(PostCreateDto request) {
        User user = userService.findUser();
        Post post = new Post(request, user);
        postRepository.save(post);
    }

    // 특정 게시물 조희
    @Transactional(readOnly = true)
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
    @Transactional
    public void updatePost(Long postId, PostUpdateRequestDto requestDto) {
        Post post = findPostByPostId(postId);
        User user = userService.findUser();

        if (!post.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("수정 권한이 없습니다.");
        }
        post.update(requestDto);
        postRepository.save(post);
    }

    // 게시글 삭제
    @Transactional
    public void deletePost(Long postId) {
        Post post = findPostByPostId(postId);
        User user = userService.findUser();

        if (!post.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("삭제 권한이 없습니다.");
        }

        postRepository.delete(post);
    }

    @Transactional
    public Post findPostByPostId(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다. postId=" + postId));
    }
}

