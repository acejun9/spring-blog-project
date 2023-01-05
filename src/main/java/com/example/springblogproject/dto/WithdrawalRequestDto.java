package com.example.springblogproject.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class WithdrawalRequestDto {
    private String password;

    public WithdrawalRequestDto(String password) {
        this.password = password;
    }
}
