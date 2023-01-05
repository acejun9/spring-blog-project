package com.example.springblogproject.dto;

import com.example.springblogproject.entity.Post;
import com.example.springblogproject.entity.Comment;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class PostResponseDto {
    private Long id;
    private String title;
    private String username;
    private String content;
    private LocalDateTime createAt;
    private LocalDateTime modifiedAt;
    private Integer postLike;
    private List<CommentResponseDto> comments;

    public PostResponseDto(Post post, List<CommentResponseDto> comments){
        this.id = post.getId();
        this.title = post.getTitle();
        this.username = post.getUsername();
        this.content = post.getContent();
        this.createAt = post.getCreatedAt();
        this.modifiedAt = post.getModifiedAt();
        this.postLike = post.getLikeCount();
        this.comments = comments;
    }
}