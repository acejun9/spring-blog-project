package com.example.springblogproject.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TokenRequestDto {
    private String refreshToken;

    public TokenRequestDto(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
