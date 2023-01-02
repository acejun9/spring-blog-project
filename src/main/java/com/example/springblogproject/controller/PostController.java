package com.example.springblogproject.controller;

import com.example.springblogproject.dto.PostRequestDto;
import com.example.springblogproject.entity.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PostController {

    @PostMapping("/api/posts")
    public Post createPost(@RequestBody PostRequestDto List<PostResponseDto>) {

    }

}
