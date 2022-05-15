package com.example.backend.diary.Diary;

import java.time.LocalDate;

import com.example.backend.music.MusicInfo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DiaryDto {

    private int diary_id;
    private String uid;
    private LocalDate diaryDate;
    private String content;
    private LocalDate regDate;
    private boolean isDeleted;
    private float happy;
    private float joy;
    private float soso;
    private float sad;
    private float angry;
    private float sentiment;
    private int mid;
    private String musicTitle;
    private String musicArtist;
    private String videoId;
    private String musicGenre;
    private String filePath;






}