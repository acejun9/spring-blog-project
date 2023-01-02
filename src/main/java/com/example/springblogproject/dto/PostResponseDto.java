package com.example.springblogproject.dto;

import com.example.springblogproject.entity.Post;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.xml.stream.events.Comment;
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

    private List<CommentListDto> comments;

    private LocalDateTime createAt;

    private LocalDateTime modifiedAt;

    private Long postLike;

    public PostResponseDto(Post post){
        this.id = post.getId();
        this.title = post.getTitle();
        this.username = post.getUsername();
        this.content = post.getContent();
        this.createAt = post.getCreatedAt();
        this.modifiedAt = post.getModifiedAt();
        this.postLike = post.getPostLike();
        List<CommentListDto> comments = new ArrayList<>();
        for (Comment comment : post.getComments()) {
            comments.add(new CommentListDto(comment));
        }
        this.comments = comments;
    }
}