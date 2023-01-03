package com.example.springblogproject.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@NoArgsConstructor
public class MsgStatusResponseDto {

    private String msg;
    private HttpStatus statusCode;

    public MsgStatusResponseDto(String msg, HttpStatus statusCode) {
        this.statusCode = statusCode;
        this.msg = msg;
    }
}
