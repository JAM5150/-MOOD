package com.example.backend.diary.Diary;

import com.example.backend.music.MusicInfo;

public class DiaryAdapter {
    public static DiaryDto entityToDto(
            DiaryAnalytics diaryAnalytics,
            DiaryAnalyticsSentiment diaryAnalyticsSentiment,
            Diary diary,
            DiaryMusic diaryMusic) {
        return new DiaryDto.DiaryDtoBuilder()
                .diary_id(diary.getDiaryId())
                .uid(diary.getUserAuth().getUid())
                .diaryDate(diary.getDiaryDate())
                .content(diary.getContent())
                .regDate(diary.getRegDate())
                .isDeleted(diary.isDeleted())
                .happy(diaryAnalytics.getHappy())
                .joy(diaryAnalytics.getJoy())
                .soso(diaryAnalytics.getSoso())
                .sad(diaryAnalytics.getSad())
                .angry(diaryAnalytics.getAngry())
                .sentiment(diaryAnalyticsSentiment.getSentiment())
                .mid(diaryMusic.getMusicInfo().getMid())
                .musicTitle(diaryMusic.getMusicInfo().getMusicTitle())
                .musicArtist(diaryMusic.getMusicInfo().getMusicArtist())
                .musicGenre(diaryMusic.getMusicInfo().getMusicGenre())
                .videoId(diaryMusic.getMusicInfo().getVideoId())
                .filePath(diaryMusic.getMusicInfo().getFilePath())
                .build();
    }
}
