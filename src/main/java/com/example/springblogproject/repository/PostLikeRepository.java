package com.example.springblogproject.repository;

import com.example.springblogproject.entity.Post;
import com.example.springblogproject.entity.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {
    Optional<PostLike> findByUsernameAndPostId(String username, Long postId);
    List<PostLike> findByUsername(String username);
    List<PostLike> findByPost(Post post);
    void deleteByUsernameAndPostId(String username, Long postId);
}
