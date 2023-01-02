package com.example.springblogproject.service;

import com.example.springblogproject.entity.Comment;
import com.example.springblogproject.entity.CommentLike;
import com.example.springblogproject.repository.CommentLikeRepository;
import com.example.springblogproject.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final CommentLikeRepository commentLikeRepository;



    @Transactional
    public String updateLikeComment(Long id, String username){
        Comment comment = commentRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("해당 글이 존재 하지 않습니다.")
        );
        for(CommentLike commentLike: comment.getCommentLikes()){
            if(commentLike.getUsername().equals(username)){
                commentLikeRepository.delete(commentLike);
                return "minus";
            }
        }
        commentLikeRepository.save(new CommentLike(comment,username));
        return "plus";
    }
}
