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

    @Column(nullable = false)
    private YearMonth yearMonth;

    @Setter
    private String statisticsComment;

    @Column(nullable = false)
    private Long joy = 0L;

    @Column(nullable = false)
    private Long excitement = 0L;

    @Column(nullable = false)
    private Long happiness = 0L;

    @Column(nullable = false)
    private Long calm = 0L;

    @Column(nullable = false)
    private Long depression = 0L;

    @Column(nullable = false)
    private Long anxiety = 0L;

    @Column(nullable = false)
    private Long sadness = 0L;

    @Column(nullable = false)
    private Long anger = 0L;

    @Builder
    public EmotionStatistics(User user, YearMonth yearMonth) {
        this.user = user;
        this.yearMonth = yearMonth;
    }

    public void increaseEmotionCount(Emotion emotion) {
        switch (emotion) {
            case JOY:
                this.joy++;
                break;
            case EXCITEMENT:
                this.excitement++;
                break;
            case HAPPINESS:
                this.happiness++;
                break;
            case CALM:
                this.calm++;
                break;
            case DEPRESSION:
                this.depression++;
                break;
            case ANXIETY:
                this.anxiety++;
                break;
            case SADNESS:
                this.sadness++;
                break;
            case ANGER:
                this.anger++;
                break;
        }
    }

    public void decreaseEmotionCount(Emotion emotion) {
        switch (emotion) {
            case JOY:
                this.joy--;
                break;
            case EXCITEMENT:
                this.excitement--;
                break;
            case HAPPINESS:
                this.happiness--;
                break;
            case CALM:
                this.calm--;
                break;
            case DEPRESSION:
                this.depression--;
                break;
            case ANXIETY:
                this.anxiety--;
                break;
            case SADNESS:
                this.sadness--;
                break;
            case ANGER:
                this.anger--;
                break;
        }
    }
}
