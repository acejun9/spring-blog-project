package com.example.springblogproject.dto;

import com.example.springblogproject.entity.User;
import com.example.springblogproject.entity.UserRoleEnum;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LoginResponseDto {

    private Long id;
    private String name;
    private String password;
    private UserRoleEnum role;

    public LoginResponseDto(User user){
        this.id = user.getId();
        this.name = user.getUsername();
        this.password = user.getPassword();
        this.role = user.getRole();
    }
}
