package com.example.springblogproject.dto;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@NoArgsConstructor
public class SignupRequestDto {


    //유효성검사
    @Pattern(regexp = "(?=.*[0-9])(?=.*[a-z])[0-9a-z]{4,10}", message = "이름은 숫자나 알파벳 소문자 4~10자리 입력해주세요")
    @NotNull(message="이름을 입력해주세요")
    private String username;

    @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[~!@#$%^&*()_+])[0-9a-zA-Z~!@#$%^&*()_+]{8,15}", message = "비밀번호는 숫자,특수문자,알파벳 8~15자리 입력해주세요")
    @NotNull(message="비밀번호를 입력해주세요")
    private String password;
    private boolean admin = false;
    private String adminToken = "";

    //Setter대신해서
    @Builder
    public SignupRequestDto(String username, String password,String adminToken)
    {
        this.username = username;
        this.password = password;
        this.adminToken = adminToken;
    }

}