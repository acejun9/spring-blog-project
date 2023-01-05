package com.example.springblogproject.service;

import com.example.springblogproject.dto.*;
import com.example.springblogproject.entity.Post;
import com.example.springblogproject.entity.RefreshToken;
import com.example.springblogproject.entity.User;
import com.example.springblogproject.jwt.JwtUtil;
import com.example.springblogproject.repository.PostRepository;
import com.example.springblogproject.repository.RefreshTokenRepository;
import com.example.springblogproject.repository.UserRepository;
import com.example.springblogproject.util.UserRoleEnum;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final PostService postService;
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

    //회원 탈퇴 시 작성한 게시글 함께 삭제
    @Transactional
    public void withdraw(String username, String password){
        User user = userRepository.findUserByUsername(username);

        if(!passwordEncoder.matches(password,user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다");
        }

        //postService의 delete기능 이용
        List<Post> postList = postRepository.findPostsByUsername(username);
        for (Post post : postList) {
            postService.deleteMyPost(post.getId(),username);
        }

        userRepository.delete(user);
    }

}
