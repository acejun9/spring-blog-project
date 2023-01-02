package com.example.springblogproject.dto;

import lombok.Getter;

import javax.xml.stream.events.Comment;
import java.time.LocalDateTime;
import java.util.List;

@Getter
public class PostResponseDto {
    private Long id;
    private String title;
    private String username;
    private List<Comment> comments;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private Long postLike;

}

//String title
//String username
//String content
//List <Comment> comments
//
//LocalDateTime createdAt
//LocalDateTime modifiedAt
//
//Long postLike