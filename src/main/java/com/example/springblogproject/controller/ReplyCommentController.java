package com.example.springblogproject.controller;

import com.example.springblogproject.dto.CommentRequestDto;
import com.example.springblogproject.dto.CommentResponseDto;
import com.example.springblogproject.dto.ReplyCommentResponseDto;
import com.example.springblogproject.security.UserDetailsImpl;
import com.example.springblogproject.service.CommentService;
import com.example.springblogproject.service.ReplyCommentService;
import com.example.springblogproject.util.UserRoleEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reply-comments")
public class ReplyCommentController {
    private final ReplyCommentService replyCommentService;

    @PostMapping("/{postId}/comments/{parentId}")
    public ResponseEntity<ReplyCommentResponseDto> createReplyComment(@PathVariable Long postId, @PathVariable Long parentId, @RequestBody CommentRequestDto commentRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return new ResponseEntity<>(replyCommentService.createReplyComment(postId, parentId, commentRequestDto, userDetails.getUsername()), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReplyCommentResponseDto> updateMyReplyCommentById(@PathVariable Long id, @RequestBody CommentRequestDto commentRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return new ResponseEntity<>(replyCommentService.updateMyReplyCommentById(id, commentRequestDto, userDetails.getUsername()), HttpStatus.OK);
    }

    @PutMapping("/admin/{id}")
    @Secured(UserRoleEnum.Authority.ADMIN) //admin인지 확인
    public ResponseEntity<ReplyCommentResponseDto> updateAdminReplyCommentById(@PathVariable Long id, @RequestBody CommentRequestDto commentRequestDto) {
        return new ResponseEntity<>(replyCommentService.updateAdminReplyCommentById(id, commentRequestDto), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteMyReplyCommentById(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        replyCommentService.deleteMyReplyCommentById(id, userDetails.getUsername());
        return new ResponseEntity<>("해당 댓글이 삭제되었습니다.", HttpStatus.OK);
    }

    @DeleteMapping("/admin/{id}")
    @Secured(UserRoleEnum.Authority.ADMIN) //admin인지 확인
    public ResponseEntity<String> deleteAdminReplyCommentById(@PathVariable Long id) {
        replyCommentService.deleteAdminReplyCommentById(id);
        return new ResponseEntity<>("해당 댓글이 삭제되었습니다.", HttpStatus.OK);
    }
}