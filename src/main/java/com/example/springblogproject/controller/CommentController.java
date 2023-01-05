package com.example.springblogproject.controller;

import com.example.springblogproject.dto.CommentRequestDto;
import com.example.springblogproject.dto.CommentResponseDto;
import com.example.springblogproject.entity.CommentLike;
import com.example.springblogproject.entity.PostLike;
import com.example.springblogproject.repository.CommentLikeRepository;
import com.example.springblogproject.security.UserDetailsImpl;
import com.example.springblogproject.service.CommentService;
import com.example.springblogproject.util.UserRoleEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comments")
public class CommentController {
    private final CommentService commentService;
    private final CommentLikeRepository commentLikeRepository;

    @PostMapping("/{postId}")
    public ResponseEntity<CommentResponseDto> createComment(@PathVariable Long postId, @RequestBody CommentRequestDto commentRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return new ResponseEntity<>(commentService.createComment(postId, commentRequestDto, userDetails.getUsername()), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CommentResponseDto> updateMyCommentById(@PathVariable Long id, @RequestBody CommentRequestDto commentRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return new ResponseEntity<>(commentService.updateMyCommentById(id, commentRequestDto, userDetails.getUsername()), HttpStatus.OK);
    }

    @PutMapping("/admin/{id}")
    @Secured(UserRoleEnum.Authority.ADMIN) //admin인지 확인
    public ResponseEntity<CommentResponseDto> updateAdminCommentById(@PathVariable Long id, @RequestBody CommentRequestDto commentRequestDto) {
        return new ResponseEntity<>(commentService.updateAdminCommentById(id, commentRequestDto), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteMyCommentById(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        commentService.deleteMyCommentById(id, userDetails.getUsername());
        return new ResponseEntity<>("해당 댓글이 삭제되었습니다.", HttpStatus.OK);
    }

    @DeleteMapping("/admin/{id}")
    @Secured(UserRoleEnum.Authority.ADMIN) //admin인지 확인
    public ResponseEntity<String> deleteAdminCommentById(@PathVariable Long id) {
        commentService.deleteAdminCommentById(id);
        return new ResponseEntity<>("해당 댓글이 삭제되었습니다.", HttpStatus.OK);
    }

    @PostMapping("/{id}/like")
    public ResponseEntity<String> likeComment(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        Optional<CommentLike> commentLike = commentLikeRepository.findByUsernameAndCommentId(userDetails.getUsername(),id);
        if(commentLike.isPresent()){
            throw new IllegalArgumentException("already did like");
        }
        return new ResponseEntity<>(commentService.likeComment(id, userDetails.getUsername()), HttpStatus.OK);
    }

    @DeleteMapping("/{id}/like")
    public ResponseEntity<String> cancelLikedComment(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        Optional<CommentLike> commentLike = commentLikeRepository.findByUsernameAndCommentId(userDetails.getUsername(),id);
        if(commentLike.isEmpty()){
            throw new IllegalArgumentException("you didn't like");
        }
        return new ResponseEntity<>(commentService.cancelLikedComment(id, userDetails.getUsername()), HttpStatus.OK);
    }
}
