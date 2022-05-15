package com.example.backend.diary.Diary;

import org.springframework.data.jpa.repository.JpaRepository;

public interface DiaryMusicDao extends JpaRepository<DiaryMusic, String>{

    DiaryMusic getDiaryMusicByDiary(Diary diary);

}