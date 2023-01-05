package com.example.springblogproject.service;

import com.example.springblogproject.dto.CommentRequestDto;
import com.example.springblogproject.dto.CommentResponseDto;
import com.example.springblogproject.dto.ReplyCommentResponseDto;
import com.example.springblogproject.entity.Comment;
import com.example.springblogproject.entity.Post;
import com.example.springblogproject.entity.ReplyComment;
import com.example.springblogproject.repository.CommentRepository;
import com.example.springblogproject.repository.PostRepository;
import com.example.springblogproject.repository.ReplyCommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReplyCommentService {
    private final ReplyCommentRepository replyCommentRepository;
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    @Transactional
    public ReplyCommentResponseDto createReplyComment(Long postId, Long parentId, CommentRequestDto commentRequestDto, String username){
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new IllegalArgumentException("해당 글이 존재 하지 않습니다.")
        );
        Comment comment = commentRepository.findById(parentId).orElseThrow(
                () -> new IllegalArgumentException("해당 댓글이 존재 하지 않습니다.")
        );

        if(!comment.getPostId().equals(post.getId())){
            throw new IllegalArgumentException("해당 글에 존재하는 댓글이 아닙니다");
        }

        ReplyComment replyComment = new ReplyComment(username, commentRequestDto.getContent(), post.getId(), parentId);
        replyCommentRepository.save(replyComment);

        return new ReplyCommentResponseDto(replyComment);
    }

    @Transactional
    public ReplyCommentResponseDto updateMyReplyCommentById(Long id, CommentRequestDto commentRequestDto, String username){
        ReplyComment replyComment = replyCommentRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("해당 댓글이 존재 하지 않습니다.")
        );

        if (!replyComment.isEqualUsername(username)) {
            throw new IllegalArgumentException("본인이 작성한 댓글이 아닙니다.");
        }

        replyComment.updateContent(commentRequestDto.getContent());
        replyCommentRepository.save(replyComment);

        return new ReplyCommentResponseDto(replyComment);
    }

    @Transactional
    public ReplyCommentResponseDto updateAdminReplyCommentById(Long id, CommentRequestDto commentRequestDto){
        ReplyComment replyComment = replyCommentRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("해당 댓글이 존재 하지 않습니다."));

        replyComment.updateContent(commentRequestDto.getContent());
        replyCommentRepository.save(replyComment);

        return new ReplyCommentResponseDto(replyComment);
    }

    @Transactional
    public void deleteMyReplyCommentById(Long id, String username){
        ReplyComment replyComment = replyCommentRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("해당 댓글이 존재 하지 않습니다."));

        if (!replyComment.isEqualUsername(username)) {
            throw new IllegalArgumentException("본인이 작성한 댓글이 아닙니다.");
        }

        replyCommentRepository.delete(replyComment);
    }

    @Transactional
    public void deleteAdminReplyCommentById(Long id){
        ReplyComment replyComment = replyCommentRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("해당 댓글이 존재 하지 않습니다."));

        replyCommentRepository.delete(replyComment);
    }
}
