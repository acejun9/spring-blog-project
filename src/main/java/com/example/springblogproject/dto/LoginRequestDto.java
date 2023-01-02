package com.example.springblogproject.dto;

import jakarta.annotation.Nonnull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.validation.constraints.NotNull;

import org.aspectj.bridge.IMessage;

@Getter
@NoArgsConstructor
public class LoginRequestDto {

    //유효성검사
    @NotNull(message="이름을 입력해주세요")
    private String username;
    @NotNull(message="비밀번호를 입력해주세요")
    private String password;

    //Setter대신 사용
    @Builder
    public LoginRequestDto(String username, String password)
    {
        this.username = username;
        this.password = password;

    }
}