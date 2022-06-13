package com.example.backend.profile;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.backend.diary.Diary.Diary;
import com.example.backend.diary.Diary.DiaryAdapter;
import com.example.backend.diary.Diary.DiaryAnalytics;
import com.example.backend.diary.Diary.DiaryAnalyticsDao;
import com.example.backend.diary.Diary.DiaryAnalyticsSentiment;
import com.example.backend.diary.Diary.DiaryAnalyticsSentimentDao;
import com.example.backend.diary.Diary.DiaryDao;
import com.example.backend.diary.Diary.DiaryDto;
import com.example.backend.diary.Diary.DiaryMusic;
import com.example.backend.diary.Diary.DiaryMusicDao;
import com.example.backend.member.MemberAuthDao;
import com.example.backend.member.UserAuth;
import com.example.backend.music.MusicAdapater;
import com.example.backend.music.MusicDto;
import com.example.backend.music.MusicInfo;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
@Slf4j
@AllArgsConstructor
@Service

public class ProfileService {
    DiaryDao diaryDao;
    MemberAuthDao memberAuthDao;
    DiaryMusicDao diaryMusicDao;
    DiaryAnalyticsDao diaryAnalyticsDao;
    DiaryAnalyticsSentimentDao diaryAnalyticsSentimentDao;
    public List<MusicDto> getMyPlayList(String id){
        Sort sort =sortByDate();
        List<MusicDto> result=new ArrayList<MusicDto>();
        UserAuth userAuth= MemberAuthDao.getUserAuthByUid(id);
        List<Diary> tmp_DiaryList=diaryDao.getDiaryByUserAuthAndIsDeletedIsFalse(userAuth,sort);
        for(int i=0;i<tmp_DiaryList.size();i++) {
            MusicDto musicDto=new MusicDto();
            DiaryMusic currentDiaryMusic=diaryMusicDao.getDiaryMusicByDiary(tmp_DiaryList.get(i));
            musicDto=MusicAdapater.entityToDto(tmp_DiaryList.get(i), currentDiaryMusic.getMusicInfo());
            result.add(musicDto);
        }
        return result;
    }

    public List<DiaryDto> getMyDiary(String id){
        Sort sort = sortByDate();
        List<DiaryDto> result =new ArrayList<DiaryDto>();
        UserAuth userAuth= MemberAuthDao.getUserAuthByUid(id);
        List<Diary> tmp_DiaryList=diaryDao.getDiaryByUserAuthAndIsDeletedIsFalse(userAuth,sort);
        for(int i=0;i<tmp_DiaryList.size();i++) {
            DiaryDto tmpDiaryDto=new DiaryDto();
            DiaryAnalytics diaryAnalytics = diaryAnalyticsDao.getDiaryAnalyticsByDiary(tmp_DiaryList.get(i));
            DiaryAnalyticsSentiment diaryAnalyticsSentiment=diaryAnalyticsSentimentDao.getDiaryAnalyticsSentimentByDiary(tmp_DiaryList.get(i));
            DiaryMusic diaryMusic=diaryMusicDao.getDiaryMusicByDiary(tmp_DiaryList.get(i));
            tmpDiaryDto=DiaryAdapter.entityToDto(diaryAnalytics, diaryAnalyticsSentiment, tmp_DiaryList.get(i), diaryMusic);
            result.add(tmpDiaryDto);
        }

        return result;
    }

    public List<DiaryAnalyticsSentiment> getMYReportSentiment(String id,LocalDate startDate,LocalDate endDate){
        List<DiaryAnalyticsSentiment> result =new ArrayList<DiaryAnalyticsSentiment>();
        UserAuth userAuth= MemberAuthDao.getUserAuthByUid(id);
        List<Diary>  tmpDiaryList=diaryDao.findDiaryByUserAuthAndIsDeletedIsFalseAndDiaryDateBetween(userAuth, startDate, endDate);
        for(int i=0;i<tmpDiaryList.size();i++) {
            DiaryAnalyticsSentiment diaryAnalyticsSentiment=diaryAnalyticsSentimentDao.getDiaryAnalyticsSentimentByDiary(tmpDiaryList.get(i));

            result.add(diaryAnalyticsSentiment);
        }
        return result;
    }
    public int getMYReportDiary(String id,LocalDate startDate,LocalDate endDate) {
        UserAuth userAuth= MemberAuthDao.getUserAuthByUid(id);
        List<Diary>  tmpDiaryList=diaryDao.findDiaryByUserAuthAndIsDeletedIsFalseAndDiaryDateBetween(userAuth, startDate, endDate);
        return tmpDiaryList.size();
    }

    private static Sort sortByDate() {
        return Sort.by(Sort.Direction.DESC,"diaryDate");
    }
}