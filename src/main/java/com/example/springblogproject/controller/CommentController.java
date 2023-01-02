package com.example.springblogproject.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comment")
public class CommentController {
    private final CommentService commentService;




    @PatchMapping("/{id}")
    public ResponseEntity<String> updateLikeComment(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return new ResponseEntity<>(commentService.updateLikeComment(id,userDetails.getUsername()), HttpStatus.OK);
    }
}
