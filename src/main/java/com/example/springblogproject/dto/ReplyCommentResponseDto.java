package com.example.springblogproject.dto;

import com.example.springblogproject.entity.Comment;
import com.example.springblogproject.entity.ReplyComment;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
public class ReplyCommentResponseDto {
    private Long id;
    private String content;
    private String username;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public ReplyCommentResponseDto(ReplyComment replyComment) {
        this.id = replyComment.getId();
        this.content = replyComment.getContent();
        this.username = replyComment.getUsername();
        this.createdAt = replyComment.getCreatedAt();
        this.modifiedAt = replyComment.getModifiedAt();
    }

}
