package com.example.springblogproject.entity;

import com.example.springblogproject.entity.CommentLike;

import com.example.springblogproject.util.Timestamped;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Comment extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column
    private String username;
    @Column
    private String content;
    @OneToMany(mappedBy = "comment", fetch = FetchType.LAZY)
    private List<CommentLike> commentLikes = new ArrayList<>();
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    public Comment(String username, String content, Post post) {
        this.username = username;
        this.content = content;
        this.post = post;
    }

    public Integer getLikeCount() {
        return commentLikes.size();
    }

    public void updateContent(String content) {
        this.content = content;
    }

    public boolean isEqualUsername(String username) {
        return this.username.equals(username);
    }

}
