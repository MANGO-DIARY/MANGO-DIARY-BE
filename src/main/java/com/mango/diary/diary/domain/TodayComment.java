package com.mango.diary.diary.domain;

import com.mango.diary.auth.domain.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor
public class TodayComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String comment;

    @Column(nullable = false)
    private LocalDate date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public TodayComment(User user) {
        String[] comments = {
                "오늘 느낀 감정, 모두 소중한 경험이에요.",
                "하루의 작은 순간들도 기록해 보세요.",
                "감정은 흐르는 물처럼 흘러가요.",
                "하루의 끝에서 자신을 돌보는 시간을 가져보세요.",
                "마음의 소리를 경청해 보세요.",
                "오늘은 어떤 감정을 만났나요?",
                "좋은 날도, 나쁜 날도 모두 특별해요.",
                "감정은 변할 수 있다는 것을 기억하세요.",
                "오늘 하루, 잘 견뎌냈어요.",
                "감정을 적는 것만으로도 치유가 시작됩니다.",
                "스스로를 위로하는 시간을 가져보세요.",
                "하루를 마무리하며 마음을 비워보세요.",
                "작은 기쁨도 큰 행복으로 만들 수 있어요.",
                "하루의 작은 성취에도 스스로를 칭찬해 주세요.",
                "감정을 표현하는 것이 결코 약한 것이 아니에요.",
                "마음속 깊은 곳에 숨어 있는 감정을 찾아보세요.",
                "오늘의 감정도 내일의 나를 만드는 과정이에요.",
                "자신을 사랑하는 마음을 잊지 마세요.",
                "마음의 평화를 찾는 시간이 필요해요.",
                "오늘의 힘든 순간도 언젠가 지나갈 거예요.",
                "감정은 일시적이라는 것을 기억하세요.",
                "자신을 위로하는 말 한마디가 큰 힘이 돼요.",
                "잠시 쉬어가는 것도 괜찮아요.",
                "오늘 하루, 스스로에게 고맙다고 말해보세요.",
                "작은 것에서 행복을 찾을 수 있어요.",
                "감정을 기록하는 것은 자기 자신을 이해하는 ,과정이에요.",
                "오늘의 눈물은 내일의 웃음이 될 수 있어요.",
                "자신의 감정을 존중하는 것이 중요해요.",
                "지금 이 순간을 살아가는 것이 중요해요."
        };
        this.user = user;
        this.date = LocalDate.now();
        this.comment = comments[new java.util.Random().nextInt(comments.length)];
    }
}
