package com.teacher.qualification.service.comment;

import com.teacher.qualification.domain.comment.Comment;
import com.teacher.qualification.domain.post.Post;
import com.teacher.qualification.domain.user.User;
import com.teacher.qualification.dto.comment.CommentInfo;
import com.teacher.qualification.dto.comment.ModifyCommentDto;
import com.teacher.qualification.dto.comment.WriteCommentDto;
import com.teacher.qualification.repository.comment.CommentRepository;
import com.teacher.qualification.repository.post.PostRepository;
import com.teacher.qualification.repository.user.UserRepository;
import com.teacher.qualification.service.post.PostService;
import com.teacher.qualification.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CommentService {
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserService userService;
    @Autowired
    private PostService postService;

    // 댓글 불러오기
    public List<CommentInfo> getAllComment(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("Post not found"));

        if (post.getComments() == null) {
            return null;
        }
        List<Comment> comments = post.getComments();
        List<CommentInfo> commentInfoList = new ArrayList<>();
        for (Comment comment : comments) {
            commentInfoList.add(convertToDto(comment));
        }
        return commentInfoList;
    }

    private CommentInfo convertToDto(Comment comment) {
        return new CommentInfo(
                comment.getUser().getId(),
                comment.getId(),
                comment.getComment(),
                comment.getUser().getNickname(),
                comment.getCreatedAt());
    }

    // 댓글 작성
    public boolean createComment(Long postId, WriteCommentDto commentDto) {
        if(!userService.isLogin()){
            return false;
        }
        User user = userService.findUser();
        Post post = postService.findPostByPostId(postId);
        Comment comment = new Comment(user, post, commentDto);
        System.out.println("입력한 댓글 : "+commentDto.comment());
        commentRepository.save(comment);
        return true;
    }

    // 댓글 수정
    public boolean updateComment(Long commentId, ModifyCommentDto commentDto) {
        Comment comment = commentRepository.getReferenceById(commentId);
        User user = userService.findUser();
        if(!comment.getUser().getId().equals(user.getId())){
            return false;
        }
        comment.setComment(commentDto.comment());
        commentRepository.save(comment);
        return true;
    }

    // 댓글 삭제
    public boolean deleteComment(Long commentId) {
        Comment comment = commentRepository.getReferenceById(commentId);
        User user = userService.findUser();
        if(!comment.getUser().getId().equals(user.getId())){
            return false;
        }
        commentRepository.delete(comment);
        return true;
    }


}

