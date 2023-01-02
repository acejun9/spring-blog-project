package com.example.springblogproject.controller;

import com.example.springblogproject.dto.PostRequestDto;
import com.example.springblogproject.dto.PostResponseDto;
import com.example.springblogproject.entity.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class PostController {

    @GetMapping("/api/posts")
    public ModelAndView posts() {
        return new ModelAndView(List<PostResponseDto>);
    }

}
