package com.example.springblogproject.entity;

import com.example.springblogproject.util.Timestamped;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Getter
@NoArgsConstructor
public class Comment extends Timestamped{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String content;
    @Column
    private String username;
    @Column
    private Integer likeCount;
    @Column
    private Long postId;

    @Builder
    public Comment(String content, String username, Long postId){
        this.likeCount = 0;
        this.content = content;
        this.username = username;
        this.postId = postId;
    }

    public void updateContent(String content){
        this.content = content;
    }

    public void plusLikeCount(){
        this.likeCount++;
    }
    public void minusLikeCount(){
        this.likeCount--;
    }
    public boolean isEqualUsername(String username){
        return this.username.equals(username);
    }
}
