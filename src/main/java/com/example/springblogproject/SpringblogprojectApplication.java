package com.example.springblogproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class SpringblogprojectApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringblogprojectApplication.class, args);
    }

}
