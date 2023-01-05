package com.example.springblogproject.entity;

import com.example.springblogproject.util.Timestamped;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class RefreshToken extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String username;
    @Column
    private String tokenValue;

    public RefreshToken(String username, String refreshToken) {
        this.username = username;
        this.tokenValue = refreshToken;
    }
}
