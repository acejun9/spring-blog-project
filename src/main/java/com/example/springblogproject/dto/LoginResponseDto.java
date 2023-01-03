package com.example.springblogproject.dto;

import com.example.springblogproject.util.UserRoleEnum;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LoginResponseDto {
    private String username;
    private UserRoleEnum role;
    @Builder
    public LoginResponseDto(String username,UserRoleEnum role) {
        this.username = username;
        this.role = role;
    }
}
