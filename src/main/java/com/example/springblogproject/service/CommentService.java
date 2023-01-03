package com.example.springblogproject.service;

import com.example.springblogproject.dto.CommentRequestDto;
import com.example.springblogproject.dto.CommentResponseDto;
import com.example.springblogproject.entity.Comment;
import com.example.springblogproject.entity.CommentLike;
import com.example.springblogproject.entity.Post;
import com.example.springblogproject.repository.CommentLikeRepository;
import com.example.springblogproject.repository.CommentRepository;
import com.example.springblogproject.repository.PostRepository;
import com.example.springblogproject.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final CommentLikeRepository commentLikeRepository;

    @Transactional
    public CommentResponseDto createComment(long postId, CommentRequestDto commentRequestDto, String username){
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new IllegalArgumentException("해당 글이 존재 하지 않습니다.")
        );

        Comment comment = new Comment(username, commentRequestDto.getContent(), post);
        commentRepository.save(comment);

        return new CommentResponseDto(comment);
    }

    @Transactional
    public CommentResponseDto updateMyCommentById(long id, CommentRequestDto commentRequestDto, String username){
        Comment comment = commentRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("해당 댓글이 존재 하지 않습니다.")
        );

        if (!comment.isEqualUsername(username)) {
            throw new IllegalArgumentException("본인이 작성한 댓글이 아닙니다.");
        }

        comment.updateContent(commentRequestDto.getContent());
        commentRepository.save(comment);

        return new CommentResponseDto(comment);
    }

    @Transactional
    public CommentResponseDto updateAdminCommentById(long id, CommentRequestDto commentRequestDto){
        Comment comment = commentRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("해당 댓글이 존재 하지 않습니다."));

        comment.updateContent(commentRequestDto.getContent());
        commentRepository.save(comment);

        return new CommentResponseDto(comment);
    }

    @Transactional
    public void deleteMyCommentById(long id, String username){
        Comment comment = commentRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("해당 댓글이 존재 하지 않습니다."));

        if (!comment.isEqualUsername(username)) {
            throw new IllegalArgumentException("본인이 작성한 댓글이 아닙니다.");
        }

        commentRepository.delete(comment);
    }

    @Transactional
    public void deleteAdminCommentById(long id){
        Comment comment = commentRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("해당 댓글이 존재 하지 않습니다."));

        commentRepository.delete(comment);
    }

    @Transactional
    public String updateLikeComment(Long id, String username){
        Comment comment = commentRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("해당 글이 존재 하지 않습니다.")
        );
        for(CommentLike commentLike: comment.getCommentLikes()){
            if(commentLike.getUsername().equals(username)){
                commentLikeRepository.delete(commentLike);
                return "minus";
            }
        }
        commentLikeRepository.save(new CommentLike(comment,username));
        return "plus";
    }
}
