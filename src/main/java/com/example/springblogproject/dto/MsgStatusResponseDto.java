package com.example.springblogproject.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@NoArgsConstructor
public class MsgStatusResponseDto {

    private String msg;
    private HttpStatus httpStatus;

    public MsgStatusResponseDto(String msg, HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
        this.msg = msg;
    }
}
