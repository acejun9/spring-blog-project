package com.example.springblogproject.entity;

import com.example.springblogproject.entity.CommentLike;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private String username;

    @Column
    private String comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @Column
    private Post post;

    @OneToOne
    @Column
    private CommentLike commentLike;

    public Comment(String username, String comment, Post post) {
        this.username = username;
        this.comment = comment;
        this.post = post;
    }

}
