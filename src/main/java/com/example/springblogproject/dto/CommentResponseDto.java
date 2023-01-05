package com.example.springblogproject.dto;

import com.example.springblogproject.entity.Comment;
import com.example.springblogproject.entity.CommentLike;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class CommentResponseDto {
    private Long id;
    private String content;
    private String username;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private Integer commentLike;
    private List<ReplyCommentResponseDto> replyComments;

    public CommentResponseDto(Comment comment, List<ReplyCommentResponseDto> replyComments) {
        this.id = comment.getId();
        this.content = comment.getContent();
        this.username = comment.getUsername();
        this.createdAt = comment.getCreatedAt();
        this.modifiedAt = comment.getModifiedAt();
        this.commentLike = comment.getLikeCount();
        this.replyComments = replyComments;
    }

}
