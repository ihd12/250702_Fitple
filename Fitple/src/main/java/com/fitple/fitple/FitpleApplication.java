package com.fitple.fitple;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing; // 추가

@SpringBootApplication
@EnableJpaAuditing
public class FitpleApplication {

    public static void main(String[] args) {
        SpringApplication.run(FitpleApplication.class, args);
    }

}
