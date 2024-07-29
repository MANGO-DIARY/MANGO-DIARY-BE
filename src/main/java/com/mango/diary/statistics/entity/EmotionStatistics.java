package com.mango.diary.statistics.entity;

import com.mango.diary.auth.domain.User;
import com.mango.diary.common.enums.Emotion;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.YearMonth;

@Entity
@NoArgsConstructor
@Getter
public class EmotionStatistics {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, unique = true)
    private YearMonth yearMonth;

    @Column(nullable = false)
    private String monthlyComment;

    @Setter
    private String statisticsComment;

    @Column(nullable = false)
    private Long 기쁨 = 0L;

    @Column(nullable = false)
    private Long 신남 = 0L;

    @Column(nullable = false)
    private Long 행복 = 0L;

    @Column(nullable = false)
    private Long 평온 = 0L;

    @Column(nullable = false)
    private Long 우울 = 0L;

    @Column(nullable = false)
    private Long 불안 = 0L;

    @Column(nullable = false)
    private Long 슬픔 = 0L;

    @Column(nullable = false)
    private Long 분노 = 0L;


    @Builder
    public EmotionStatistics(User user, YearMonth yearMonth, String monthlyComment) {
        this.user = user;
        this.yearMonth = yearMonth;
        this.monthlyComment = monthlyComment;
    }

    public void increaseEmotionCount(Emotion emotion) {
        switch (emotion) {
            case 기쁨:
                this.기쁨++;
                break;
            case 신남:
                this.신남++;
                break;
            case 행복:
                this.행복++;
                break;
            case 평온:
                this.평온++;
                break;
            case 우울:
                this.우울++;
                break;
            case 불안:
                this.불안++;
                break;
            case 슬픔:
                this.슬픔++;
                break;
            case 분노:
                this.분노++;
                break;
        }
    }

    public void decreaseEmotionCount(Emotion emotion) {
        switch (emotion) {
            case 기쁨:
                this.기쁨--;
                break;
            case 신남:
                this.신남--;
                break;
            case 우울:
                this.우울--;
                break;
            case 불안:
                this.불안--;
                break;
            case 슬픔:
                this.슬픔--;
                break;
            case 행복:
                this.행복--;
                break;
            case 평온:
                this.평온--;
                break;
            case 분노:
                this.분노--;
                break;
        }
    }
}
