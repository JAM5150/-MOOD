package com.example.backend.main;

import com.example.backend.diary.Diary.Diary;
import com.example.backend.diary.Diary.DiaryAnalyticsSentiment;
import com.example.backend.music.MusicInfo;

public class MainDiaryAdapter {
    public static MainDiaryDto entityToDto(Diary diary, DiaryAnalyticsSentiment diaryAnalyticsSentiment, MusicInfo musicInfo){
        return new MainDiaryDto.MainDiaryDtoBuilder()
                .date(diary.getDiaryDate().toString())
                .sentiment(diaryAnalyticsSentiment.getSentiment())
                .music_title(musicInfo.getMusicTitle())
                .music_artist(musicInfo.getMusicArtist())
                .build();
    }
}
