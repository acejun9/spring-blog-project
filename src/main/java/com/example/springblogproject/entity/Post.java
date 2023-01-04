package com.example.springblogproject.entity;

import com.example.springblogproject.util.Timestamped;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
public class Post extends Timestamped{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String title;
    @Column
    private String content;
    @Column
    private String username;
    @Column
    private Integer likeCount;

    @Builder
    public Post(String title, String content, String username){
        this.likeCount = 0;
        this.title = title;
        this.content = content;
        this.username = username;
    }

    public void update(String title, String content){
        this.title = title;
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


