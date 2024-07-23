package com.mango.diary.diary.domain;


import jakarta.persistence.*;

import java.time.LocalDate;

@Table(name = "diary")
@Entity
public class Diary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private String emotion;

    @Column(name = "user_id")
    private Long user;
}
