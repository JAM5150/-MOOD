package com.example.backend.diary.Diary;

import java.time.LocalDate;

import com.example.backend.member.UserAuth;
import org.springframework.data.domain.Sort;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface DiaryDao extends JpaRepository<Diary, String>{

    Diary getDiaryByUserAuthAndDiaryDateAndIsDeletedIsFalse(UserAuth userAuth,LocalDate diaryDate);

    Diary getDiaryByDiaryId(int diaryId);

    int countByUserAuthAndIsDeletedIsFalse(UserAuth userAuth);

    List<Diary> findDiaryByUserAuthAndIsDeletedIsFalseAndDiaryDateBetween(UserAuth userAuth, LocalDate start, LocalDate end);

    List<Diary> getDiaryByUserAuthAndIsDeletedIsFalse(UserAuth userAuth, Sort sort);
}