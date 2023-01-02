package com.example.springblogproject.dto;

import com.example.springblogproject.entity.User;
import com.example.springblogproject.entity.UserRoleEnum;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LoginResponseDto {
    private String name;
    private UserRoleEnum role;
    @Builder
    public LoginResponseDto(User user) {
        this.name = user.getUsername();
        this.role = user.getRole();
    }
}
