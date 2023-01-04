package com.example.springblogproject.entity;

import com.example.springblogproject.util.Timestamped;
import com.example.springblogproject.util.UserRoleEnum;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@Entity(name="users")
@NoArgsConstructor
public class User extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String username;
    @Column
    private String password;

    @Column
    @Enumerated(value = EnumType.STRING)
    private UserRoleEnum role;

    @Builder
    public User(String username, String password , UserRoleEnum role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }
}
