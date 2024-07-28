package com.mango.diary;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class MangoDiaryApplication {
    public static void main(String[] args) {
        SpringApplication.run(MangoDiaryApplication.class, args);
    }

}
