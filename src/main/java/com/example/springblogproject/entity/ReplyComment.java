package com.example.springblogproject.entity;

import com.example.springblogproject.util.Timestamped;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Getter
@NoArgsConstructor
public class ReplyComment extends Timestamped{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String username;
    @Column
    private String content;
    @Column
    private Long postId;
    @Column
    private Long parentId;

    @Builder
    public ReplyComment(String username, String content, Long postId, Long parentId){
        this.content = content;
        this.username = username;
        this.postId = postId;
        this.parentId = parentId;
    }

    public void updateContent(String content){
        this.content = content;
    }

    public boolean isEqualUsername(String username){
        return this.username.equals(username);
    }
}
