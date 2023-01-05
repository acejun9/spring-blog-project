package com.example.springblogproject.repository;

import com.example.springblogproject.entity.Comment;
import com.example.springblogproject.entity.CommentLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {
    Optional<CommentLike> findByUsernameAndCommentId(String username, Long commentId);
    List<CommentLike> findByUsername(String username);
    List<CommentLike> findByComment(Comment comment);
    void deleteByUsernameAndCommentId(String username, Long commentId);
}
