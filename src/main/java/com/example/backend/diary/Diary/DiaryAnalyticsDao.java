package com.example.backend.diary.Diary;

import org.springframework.data.jpa.repository.JpaRepository;

public interface DiaryAnalyticsDao extends JpaRepository<DiaryAnalytics, String>{

    DiaryAnalytics getDiaryAnalyticsByDiary(Diary diary);

}