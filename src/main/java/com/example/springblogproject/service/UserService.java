package com.example.springblogproject.service;

import com.example.springblogproject.dto.*;
import com.example.springblogproject.entity.RefreshToken;
import com.example.springblogproject.entity.User;
import com.example.springblogproject.jwt.JwtUtil;
import com.example.springblogproject.repository.RefreshTokenRepository;
import com.example.springblogproject.repository.UserRepository;
import com.example.springblogproject.util.UserRoleEnum;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void signup(SignupRequestDto signupRequestDto, UserRoleEnum role){
        String username = signupRequestDto.getUsername();
        String password = passwordEncoder.encode(signupRequestDto.getPassword());

        Optional<User> found = userRepository.findByUsername(username);
        if(found.isPresent()){
            throw new IllegalArgumentException("중복된 사용자가 존재합니다");
        }

        User user = new User(username, password, role);
        userRepository.saveAndFlush(user);
    }

    @Transactional(readOnly = true)
    public LoginResponseDto login(LoginRequestDto loginRequestDto){
        String username = loginRequestDto.getUsername();
        String password = loginRequestDto.getPassword();

        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new IllegalArgumentException("등록된 사용자가 없습니다")
        );

        if(!passwordEncoder.matches(password,user.getPassword())){
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다");
        }

        return new LoginResponseDto(username,user.getRole());
    }
    @Transactional
    public TokenResponseDto createToken(String username){
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new IllegalArgumentException("등록된 사용자가 없습니다")
        );

        String accessToken = jwtUtil.createAccessToken(user.getUsername(),user.getRole());
        String refreshToken = jwtUtil.createRefreshToken(user.getUsername());
        RefreshToken refreshTokenEntity = new RefreshToken(user.getUsername(),refreshToken);
        refreshTokenRepository.deleteByUsername(user.getUsername());
        refreshTokenRepository.save(refreshTokenEntity);

        return new TokenResponseDto(accessToken,refreshToken);
    }

    @Transactional
    public TokenResponseDto reissueToken(String refreshToken){
        String refreshTokenValue = refreshToken.substring(7);
        String username = jwtUtil.getUserInfoFromToken(refreshTokenValue).getSubject();
        RefreshToken findRefreshToken = refreshTokenRepository.findByTokenValue(refreshToken).orElseThrow(
                () -> new EntityNotFoundException("Refresh Token Error")
        );
        return createToken(username);
    }
}
