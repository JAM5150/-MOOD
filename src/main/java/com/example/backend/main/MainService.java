package com.example.backend.main;

import com.example.backend.diary.Diary.Diary;
import com.example.backend.diary.Diary.DiaryAnalyticsSentiment;
import com.example.backend.diary.Diary.DiaryAnalyticsSentimentDao;
import com.example.backend.diary.Diary.DiaryDao;
import com.example.backend.error.CustomException;
import com.example.backend.error.ErrorCode;
import com.example.backend.member.MemberAuthDao;
import com.example.backend.member.UserAuth;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@AllArgsConstructor
@Service
public class MainService {

    private static MemberAuthDao memberAuthDao;
    private static DiaryDao diaryDao;
    private DiaryAnalyticsSentimentDao diaryAnalyticsSentimentDao;


    public static List<Diary> getMontlyDiary(String uid, LocalDate start, LocalDate end) throws CustomException {
        UserAuth userAuth = memberAuthDao.findById(uid)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
        List<Diary> diary = diaryDao.findDiaryByUserAuthAndIsDeletedIsFalseAndDiaryDateBetween(userAuth, start, end);

        return diary;
    }

}