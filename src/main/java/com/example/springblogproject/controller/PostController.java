package com.example.springblogproject.controller;

import com.example.springblogproject.dto.PostRequestDto;
import com.example.springblogproject.dto.PostResponseDto;
import com.example.springblogproject.entity.PostLike;
import com.example.springblogproject.repository.PostLikeRepository;
import com.example.springblogproject.security.UserDetailsImpl;
import com.example.springblogproject.service.PostService;
import com.example.springblogproject.util.UserRoleEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class PostController {
    private final PostService postService;
    private final PostLikeRepository postLikeRepository;

    @GetMapping("")
    public ResponseEntity<List<PostResponseDto>> getAllPost(@PageableDefault(size = 10, page = 0, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return new ResponseEntity<>(postService.getAllPost(pageable), HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<PostResponseDto> createPost(@Validated @RequestBody PostRequestDto postRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return new ResponseEntity<>(postService.createPost(postRequestDto, userDetails.getUsername()),HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostResponseDto> getSelectPost(@PathVariable Long id){
        return new ResponseEntity<>(postService.getPost(id),HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PostResponseDto> updateMyPost(
            @PathVariable Long id, @RequestBody @Validated PostRequestDto postRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails){
        String username = userDetails.getUsername();
        return new ResponseEntity<>(postService.updateMyPost(id, postRequestDto, username),HttpStatus.OK);
    }

    @PutMapping("/admin/{id}")
    @Secured(UserRoleEnum.Authority.ADMIN)
    public ResponseEntity<PostResponseDto> updateAdminPost(
            @PathVariable Long id, @RequestBody @Validated PostRequestDto postRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return new ResponseEntity<>(postService.updateAdminPost(id, postRequestDto),HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteMyPost(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails){
        String username = userDetails.getUsername();
        postService.deleteMyPost(id, username);
        return new ResponseEntity<>("delete success",HttpStatus.OK);
    }

    @DeleteMapping("/admin/{id}")
    @Secured(UserRoleEnum.Authority.ADMIN)
    public ResponseEntity<String> deleteAdminPost(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails){
        postService.deleteAdminPost(id);
        return new ResponseEntity<>("delete success",HttpStatus.OK);
    }

    @PostMapping("/{id}/like")
    public ResponseEntity<String> likePost(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails){
        Optional<PostLike> postLike = postLikeRepository.findByUsernameAndPostId(userDetails.getUsername(),id);
        if(postLike.isPresent()){
            throw new IllegalArgumentException("already did like");
        }
        return new ResponseEntity<>(postService.likePost(id,userDetails.getUsername()),HttpStatus.OK);
    }

    @DeleteMapping("/{id}/like")
    public ResponseEntity<String> cancelLikedPost(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails){
        Optional<PostLike> postLike = postLikeRepository.findByUsernameAndPostId(userDetails.getUsername(),id);
        if(postLike.isEmpty()){
            throw new IllegalArgumentException("you didn't like");
        }
        return new ResponseEntity<>(postService.cancelLikedPost(id,userDetails.getUsername()),HttpStatus.OK);
    }
}
