package com.example.springblogproject.controller;


import com.example.springblogproject.dto.LoginRequestDto;
import com.example.springblogproject.dto.LoginResponseDto;
import com.example.springblogproject.dto.SignupRequestDto;
import com.example.springblogproject.entity.User;
import com.example.springblogproject.service.UserService;
import com.example.springblogproject.util.UserRoleEnum;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.springblogproject.repository.UserRepository;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    private static final String ADMIN_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC";

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@Validated @RequestBody SignupRequestDto signupRequestDto){

        userService.signup(signupRequestDto,UserRoleEnum.USER);

        return new ResponseEntity<>("signup success", HttpStatus.OK);
    }

    @PostMapping("/admin/signup")
    public ResponseEntity<String> adminSignup(@Validated @RequestBody SignupRequestDto signupRequestDto){
        //토큰검사
        if (!(signupRequestDto.getAdminToken().equals(ADMIN_TOKEN))) {
            throw new IllegalArgumentException("관리자 암호가 잘못되어 등록이 불가능합니다.");
        }

        userService.signup(signupRequestDto, UserRoleEnum.ADMIN);

        return new ResponseEntity<>("signup success", HttpStatus.OK);
    }


    @PostMapping("/login")
    public ResponseEntity<String> login(@Validated @RequestBody LoginRequestDto loginRequestDto){
        LoginResponseDto loginResponseDto = userService.login(loginRequestDto);
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set(jwtUtil.AUTHORIZATION_HEADER,jwtUtil.createToken(loginResponseDto.getUsername(),loginResponseDto.getRole()));
        return new ResponseEntity<>("login success",responseHeaders,HttpStatus.OK);
    }
}
