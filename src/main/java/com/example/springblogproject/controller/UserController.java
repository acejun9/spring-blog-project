package com.example.springblogproject.controller;

import com.example.springblogproject.dto.*;
import com.example.springblogproject.entity.RefreshToken;
import com.example.springblogproject.jwt.JwtUtil;
import com.example.springblogproject.repository.RefreshTokenRepository;
import com.example.springblogproject.security.UserDetailsImpl;
import com.example.springblogproject.service.UserService;
import com.example.springblogproject.util.UserRoleEnum;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;
    private final JwtUtil jwtUtil;
    private static final String ADMIN_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC";

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@Validated @RequestBody SignupRequestDto signupRequestDto) {
        userService.signup(signupRequestDto, UserRoleEnum.USER);
        return new ResponseEntity<>("signup success", HttpStatus.OK);
    }

    @PostMapping("/admin/signup")
    public ResponseEntity<String> adminSignup(@Validated @RequestBody SignupRequestDto signupRequestDto) {
        //토큰검사
        if (!signupRequestDto.getAdminToken().equals(ADMIN_TOKEN)) {
            throw new IllegalArgumentException("관리자 암호가 잘못되어 등록이 불가능합니다.");
        }
        userService.signup(signupRequestDto, UserRoleEnum.ADMIN);
        return new ResponseEntity<>("signup success", HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@Validated @RequestBody LoginRequestDto loginRequestDto) {
        LoginResponseDto loginResponseDto = userService.login(loginRequestDto);
        TokenResponseDto tokenResponseDto = userService.createToken(loginResponseDto.getUsername());
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set(jwtUtil.AUTHORIZATION_HEADER, tokenResponseDto.getAccessToken());
        responseHeaders.set(jwtUtil.REFRESHTOKEN_HEADER, tokenResponseDto.getRefreshToken());
        return new ResponseEntity<>("login success", responseHeaders, HttpStatus.OK);
    }

    @PostMapping("/reissue")
    public ResponseEntity<TokenResponseDto> reissue(HttpServletRequest request) {
        TokenResponseDto tokenResponseDto = userService.reissueToken(request.getHeader(jwtUtil.REFRESHTOKEN_HEADER).substring(7));
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set(jwtUtil.AUTHORIZATION_HEADER, tokenResponseDto.getAccessToken());
        responseHeaders.set(jwtUtil.REFRESHTOKEN_HEADER, tokenResponseDto.getRefreshToken());
        return new ResponseEntity<>(tokenResponseDto, responseHeaders, HttpStatus.OK);
    }

    @DeleteMapping("/withdrawal")
    public ResponseEntity<String> withdraw(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody WithdrawalRequestDto withdrawalRequestDto){
        userService.withdraw(userDetails.getUsername(), withdrawalRequestDto.getPassword());
        return new ResponseEntity<>("회원 탈퇴가 완료되었습니다.",HttpStatus.OK);
    }

}
