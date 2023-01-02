package com.example.springblogproject.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;



@Getter
@NoArgsConstructor
public class PostRequestDto {
    @NotBlank(message = "제목을 입력해 주세요")
    private String title;
    @NotBlank(message = "내용을 입력해 주세요")
    private String content;

    @Builder
    public PostRequestDto(String title, String content){
        this.title = title;
        this.content = content;
    }
}

