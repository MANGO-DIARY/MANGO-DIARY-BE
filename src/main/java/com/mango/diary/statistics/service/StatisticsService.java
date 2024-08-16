package com.mango.diary.statistics.service;

import com.mango.diary.diary.domain.AiComment;
import com.mango.diary.diary.domain.Diary;
import com.mango.diary.diary.repository.DiaryRepository;
import com.mango.diary.statistics.dto.EmotionCounts;
import com.mango.diary.statistics.dto.StatisticsResponse;
import com.mango.diary.statistics.entity.EmotionStatistics;
import com.mango.diary.statistics.exception.StatisticsErrorCode;
import com.mango.diary.statistics.exception.StatisticsException;
import com.mango.diary.statistics.repository.StatisticsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StatisticsService {

    private final StatisticsRepository statisticsRepository;
    private final DiaryRepository diaryRepository;

    public StatisticsResponse getStatistics(Long userId, YearMonth yearMonth) {
        YearMonth now = YearMonth.now();

        EmotionStatistics emotionStatistics = statisticsRepository.findByUserIdAndYearMonth(userId, yearMonth)
                .orElseThrow(() -> new StatisticsException(StatisticsErrorCode.STATISTICS_NOT_FOUND));

        String statisticsComment;

        if (emotionStatistics.getStatisticsComment() == null) {
            if (emotionStatistics.getYearMonth().equals(now)) {
                statisticsComment = "이번 달도 열심히 기록해봐요!";
            } else {
                statisticsComment = generateStatisticsComment(userId, emotionStatistics);
                emotionStatistics.setStatisticsComment(statisticsComment);
                statisticsRepository.save(emotionStatistics);
            }
        } else {
            statisticsComment = emotionStatistics.getStatisticsComment();
        }

        List<String> aiComments = getAiComments(userId, yearMonth);

        return new StatisticsResponse(
                emotionStatistics.getYearMonth(),
                new EmotionCounts(
                        emotionStatistics.getJoy(),
                        emotionStatistics.getExcitement(),
                        emotionStatistics.getHappiness(),
                        emotionStatistics.getCalm(),
                        emotionStatistics.getSadness(),
                        emotionStatistics.getAnger(),
                        emotionStatistics.getAnxiety(),
                        emotionStatistics.getDepression()
                ),
                aiComments,
                statisticsComment
        )
                ;
    }

    private List<String> getAiComments(Long userId, YearMonth yearMonth) {
        List<Diary> diaries = diaryRepository.findAllByDateBetweenAndUserId(yearMonth.atDay(1), yearMonth.atEndOfMonth(), userId);
        List<String> aiComments = new ArrayList<>();
        for (Diary diary : diaries) {
            // AI 댓글 객체를 가져올 때 null 체크
            AiComment aiCommentObj = diary.getAiComments();
            if (aiCommentObj != null) {
                String aiComment = aiCommentObj.getContent(); // AI 댓글 내용 가져오기
                if (aiComment != null && !aiComment.isEmpty()) {
                    aiComments.add(aiComment);
                }
            }
        }
        return aiComments;
    }

    private String generateStatisticsComment(Long userId, EmotionStatistics emotionStatistics) {
        YearMonth yearMonth = emotionStatistics.getYearMonth().minusMonths(1);
        Optional<EmotionStatistics> lastMonthStatistics = statisticsRepository.findByUserIdAndYearMonth(userId, yearMonth);

        if (lastMonthStatistics.isEmpty()) {
            Long thisMonthPositiveTotal = emotionStatistics.getJoy() + emotionStatistics.getExcitement() + emotionStatistics.getHappiness() + emotionStatistics.getCalm();
            Long thisMonthNegativeTotal = emotionStatistics.getSadness() + emotionStatistics.getAnger() + emotionStatistics.getAnxiety() + emotionStatistics.getDepression();
            return statisticsOnlyThisMonth(thisMonthPositiveTotal, thisMonthNegativeTotal);
        }

        return lastMonthExists(emotionStatistics, lastMonthStatistics.get());
    }

    private String lastMonthExists(EmotionStatistics thisMonth, EmotionStatistics lastMonth) {
        Long thisMonthPositiveTotal = thisMonth.getJoy() + thisMonth.getExcitement() + thisMonth.getHappiness() + thisMonth.getCalm();
        Long lastMonthPositiveTotal = lastMonth.getJoy() + lastMonth.getExcitement() + lastMonth.getHappiness() + lastMonth.getCalm();

        Long thisMonthNegativeTotal = thisMonth.getSadness() + thisMonth.getAnger() + thisMonth.getAnxiety() + thisMonth.getDepression();
        Long lastMonthNegativeTotal = lastMonth.getSadness() + lastMonth.getAnger() + lastMonth.getAnxiety() + lastMonth.getDepression();

        long positiveIncrease = thisMonthPositiveTotal - lastMonthPositiveTotal;
        long negativeDecrease = thisMonthNegativeTotal - lastMonthNegativeTotal;

        if (positiveIncrease <= 0 && negativeDecrease <= 0) {
            return statisticsOnlyThisMonth(thisMonthPositiveTotal, thisMonthNegativeTotal);
        }
        if (positiveIncrease >= negativeDecrease) {
            return "지난 달보다 긍정적인 일기를 " + positiveIncrease + "개 더 썼어요!";
        } else {
            return "지난 달보다 부정적인 일기를 " + negativeDecrease + "개 덜 썼어요!";
        }
    }


    private String statisticsOnlyThisMonth(Long thisMonthPositiveTotal, Long thisMonthNegativeTotal) {
        if (thisMonthPositiveTotal > thisMonthNegativeTotal) {
            return "이번달은 긍정적인 일기를 더 많이 썼어요!";
        } else if (thisMonthNegativeTotal.equals(thisMonthPositiveTotal)) {
            return "이번달의 감정을 되돌아봐요!";
        } else {
            return "이번달의 부정적인 일기가 더 많아요!";
        }
    }
}
