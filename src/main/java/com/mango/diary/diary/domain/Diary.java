package com.mango.diary.diary.domain;


import com.mango.diary.common.enums.Emotion;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "diary")
@Entity
public class Diary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private Emotion emotion;

    @Column(name = "user_id") // auth 생성되면 1대다로 수정
    private Long user;
}
