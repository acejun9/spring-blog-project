package com.example.springblogproject.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class CommentLike {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private String commentId;

    //User로 받아올지?? String으로 해서 username만 받아올지??
    //이게 맞나 모지??
    @Column
    private List<String> likeUsers = new ArrayList<>();

    @Column
    private Long likeCount;

    //일단 코멘트 만들어보고 추가하기
    public CommentLike(String commentId, String username) {
        this.commentId = commentId;
        this.likeUsers = new ArrayList<>();
        likeUsers.add(username); // ????
    }
}
