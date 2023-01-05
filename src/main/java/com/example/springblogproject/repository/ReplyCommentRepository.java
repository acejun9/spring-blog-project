package com.example.springblogproject.repository;

import com.example.springblogproject.entity.Comment;
import com.example.springblogproject.entity.ReplyComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReplyCommentRepository extends JpaRepository<ReplyComment, Long> {
    List<ReplyComment> findByParentIdOrderByCreatedAtDesc(Long parentId);
    List<ReplyComment> findByParentId(Long parentId);

    void deleteByParentId(Long parentId);
}
