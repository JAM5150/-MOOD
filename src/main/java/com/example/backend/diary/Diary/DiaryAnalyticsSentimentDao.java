package com.example.backend.diary.Diary;

import org.springframework.data.jpa.repository.JpaRepository;

public interface DiaryAnalyticsSentimentDao extends JpaRepository<DiaryAnalyticsSentiment, String>{

    DiaryAnalyticsSentiment getDiaryAnalyticsSentimentByDiary(Diary diary);
}