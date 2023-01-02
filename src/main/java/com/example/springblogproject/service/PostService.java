package com.example.springblogproject.service;

import com.example.springblogproject.entity.Post;
import com.example.springblogproject.entity.PostLike;
import com.example.springblogproject.repository.PostLikeRepository;
import com.example.springblogproject.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final PostLikeRepository postLikeRepository;




    @Transactional
    public String updateLikePost(Long id, String username){
        Post post = postRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("해당 글이 존재 하지 않습니다.")
        );
        for(PostLike postLike: post.getPostLikes()){
            if(postLike.getUsername().equals(username)){
                postLikeRepository.delete(postLike);
                return "minus";
            }
        }
        postLikeRepository.save(new PostLike(post,username));
        return "plus";
    }
}
