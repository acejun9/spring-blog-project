package com.example.springblogproject.repository;

import com.example.springblogproject.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPostIdOrderByCreatedAtDesc(Long postId);
    List<Comment> findByPostId(Long postId);
}
