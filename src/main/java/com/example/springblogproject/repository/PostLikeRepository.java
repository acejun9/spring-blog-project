package com.example.springblogproject.repository;

import com.example.springblogproject.entity.Post;
import com.example.springblogproject.entity.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {
    Optional<PostLike> findByUsernameAndPostId(String username, Long postId);
    void deleteByUsernameAndPost(String username, Post post);
}
