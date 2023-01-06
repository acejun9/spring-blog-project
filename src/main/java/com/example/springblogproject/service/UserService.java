package com.example.springblogproject.service;

import com.example.springblogproject.dto.*;
import com.example.springblogproject.entity.*;
import com.example.springblogproject.jwt.JwtUtil;
import com.example.springblogproject.repository.*;
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
    private final CommentRepository commentRepository;
    private final ReplyCommentRepository replyCommentRepository;
    private final CommentLikeRepository commentLikeRepository;
    private final PostLikeRepository postLikeRepository;
    private final CommentService commentService;
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
        RefreshToken refreshTokenEntity = new RefreshToken(user.getUsername(),refreshToken.substring(7));
        refreshTokenRepository.deleteByUsername(user.getUsername());
        refreshTokenRepository.save(refreshTokenEntity);

        return new TokenResponseDto(accessToken,refreshToken);
    }

    @Transactional
    public TokenResponseDto reissueToken(String refreshToken){
        String username = jwtUtil.getUserInfoFromToken(refreshToken).getSubject();
        RefreshToken findRefreshToken = refreshTokenRepository.findByTokenValue(refreshToken).orElseThrow(
                () -> new EntityNotFoundException("Refresh Token Error")
        );
        return createToken(username);
    }

    //회원 탈퇴 시 작성한 게시글 함께 삭제
    @Transactional
    public void withdraw(String username, String password){
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new IllegalArgumentException("등록된 사용자가 없습니다")
        );

        if(!passwordEncoder.matches(password,user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다");
        }

        //User 관련 데이터 전부 삭제
        for(PostLike postLike: postLikeRepository.findByUsername(username)){
            postLikeRepository.delete(postLike);
            postLike.getPost().minusLikeCount();
        }
        for(CommentLike commentLike: commentLikeRepository.findByUsername(username)){
            commentLikeRepository.delete(commentLike);
            commentLike.getComment().minusLikeCount();
        }
        for(ReplyComment replyComment: replyCommentRepository.findByUsername(username)){
            replyCommentRepository.delete(replyComment);
        }
        for(Comment comment: commentRepository.findByUsername(username)){
            commentService.deleteMyCommentById(comment.getId(),username);
        }
        for (Post post : postRepository.findPostsByUsername(username)) {
            postService.deleteMyPost(post.getId(),username);
        }
        refreshTokenRepository.deleteByUsername(username);
        userRepository.delete(user);
    }

}
