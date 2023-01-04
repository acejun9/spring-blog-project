package com.example.springblogproject.service;

import com.example.springblogproject.dto.CommentRequestDto;
import com.example.springblogproject.dto.CommentResponseDto;
import com.example.springblogproject.entity.Comment;
import com.example.springblogproject.entity.CommentLike;
import com.example.springblogproject.entity.Post;
import com.example.springblogproject.repository.CommentLikeRepository;
import com.example.springblogproject.repository.CommentRepository;
import com.example.springblogproject.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

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

        Comment comment = new Comment(username, commentRequestDto.getContent(), post.getId());
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
                () -> new IllegalArgumentException("해당 댓글이 존재 하지 않습니다.")
        );

        Optional<CommentLike> commentLike = commentLikeRepository.findByUsernameAndCommentId(username, id);
        if(commentLike.isPresent()) {
            comment.minusLikeCount();
            commentLikeRepository.deleteByUsernameAndComment(username, comment);
            return "Like -1";
        } else {
            comment.plusLikeCount();
            commentLikeRepository.save(new CommentLike(comment, username));
            return "Like +1";
        }
    }
}
