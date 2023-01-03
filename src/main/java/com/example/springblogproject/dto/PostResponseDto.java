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

    private List<CommentResponseDto> comments;

    private LocalDateTime createAt;

    private LocalDateTime modifiedAt;

    private Integer postLike;

    public PostResponseDto(Post post){
        this.id = post.getId();
        this.title = post.getTitle();
        this.username = post.getUsername();
        this.content = post.getContent();
        this.createAt = post.getCreatedAt();
        this.modifiedAt = post.getModifiedAt();
        this.postLike = post.getLikeCount();
        List<CommentResponseDto> comments = new ArrayList<>();
        for (Comment comment : post.getComments()) {
            comments.add(new CommentResponseDto(comment));
        }
        this.comments = comments;
    }
}