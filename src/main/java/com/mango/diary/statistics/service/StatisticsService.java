package com.mango.diary.statistics.service;

import com.mango.diary.statistics.dto.StatisticsResponse;
import com.mango.diary.statistics.entity.EmotionStatistics;
import com.mango.diary.statistics.exception.StatisticsErrorCode;
import com.mango.diary.statistics.exception.StatisticsException;
import com.mango.diary.statistics.repository.StatisticsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.YearMonth;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StatisticsService {

    private final StatisticsRepository statisticsRepository;

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

        return new StatisticsResponse(
                emotionStatistics.getYearMonth(),
                emotionStatistics.get기쁨(),
                emotionStatistics.get신남(),
                emotionStatistics.get행복(),
                emotionStatistics.get평온(),
                emotionStatistics.get슬픔(),
                emotionStatistics.get분노(),
                emotionStatistics.get불안(),
                emotionStatistics.get우울(),
                emotionStatistics.getMonthlyComment(),
                statisticsComment
        )
                ;
    }

    private String generateStatisticsComment(Long userId, EmotionStatistics emotionStatistics) {
        YearMonth yearMonth = emotionStatistics.getYearMonth().minusMonths(1);
        Optional<EmotionStatistics> lastMonthStatistics = statisticsRepository.findByUserIdAndYearMonth(userId, yearMonth);

        if (lastMonthStatistics.isEmpty()) {
            Long thisMonthPositiveTotal = emotionStatistics.get기쁨() + emotionStatistics.get신남() + emotionStatistics.get행복() + emotionStatistics.get평온();
            Long thisMonthNegativeTotal = emotionStatistics.get슬픔() + emotionStatistics.get분노() + emotionStatistics.get불안() + emotionStatistics.get우울();
            return statisticsOnlyThisMonth(thisMonthPositiveTotal, thisMonthNegativeTotal);
        }

        return lastMonthExists(emotionStatistics, lastMonthStatistics.get());

    }

    private String lastMonthExists(EmotionStatistics thisMonth, EmotionStatistics lastMonth) {
        Long thisMonthPositiveTotal = thisMonth.get기쁨() + thisMonth.get신남() + thisMonth.get행복() + thisMonth.get평온();
        Long lastMonthPositiveTotal = lastMonth.get기쁨() + lastMonth.get신남() + lastMonth.get행복() + lastMonth.get평온();

        Long thisMonthNegativeTotal = thisMonth.get슬픔() + thisMonth.get분노() + thisMonth.get불안() + thisMonth.get우울();
        Long lastMonthNegativeTotal = lastMonth.get슬픔() + lastMonth.get분노() + lastMonth.get불안() + lastMonth.get우울();

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
