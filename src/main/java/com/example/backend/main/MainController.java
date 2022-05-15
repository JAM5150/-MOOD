package com.example.backend.main;

import  com.example.backend.diary.Diary.Diary;
import com.example.backend.diary.Diary.DiaryAnalyticsSentiment;
import com.example.backend.diary.Diary.DiaryAnalyticsSentimentDao;
import com.example.backend.diary.Diary.DiaryService;
import com.example.backend.error.CustomException;
import com.example.backend.music.MusicInfo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.*;

@Slf4j
@RestController
@CrossOrigin
@AllArgsConstructor
@RequestMapping("/main")
public class MainController {

    private MainService mainService;
    private DiaryService diaryService;

    @GetMapping("/{id}/{startDate}/{endDate}")
    public ResponseEntity<Map<String, Object>> MonthlyDiaryInfo(@PathVariable String id,
                                                                @PathVariable String startDate,
                                                                @PathVariable String endDate) throws CustomException {
        HashMap<String, Object> resultMap = new HashMap<>();

        //1. uid와 month로 diary 테이블에서 diaryid와 일기 작성 날짜 가져오기 (리스트, is_deleted=false)
        //2. diaryid로 diaryanalyticssentiment 가져오기 (긍부정, 정확도)
        //3. diaryid로 music_info 가져오기
        List<MainDiaryDto> resultList = new ArrayList<>();

        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);

        List<Diary> diaryList = mainService.getMontlyDiary(id, start, end);
        if(diaryList.isEmpty()){
            resultMap.put("message", "해당 사용자의 일기 정보가 존재하지 않습니다.");
            return new ResponseEntity<>(resultMap, HttpStatus.OK);
        }

        for(Diary diary : diaryList){
            MainDiaryDto mainDiaryDto = new MainDiaryDto();

            int diaryId = diary.getDiaryId();
            DiaryAnalyticsSentiment diaryAnalyticsSentiment = diaryService.getDiaryAnalyticsSentiment(diaryId);
            MusicInfo musicInfo = diaryService.getMusicInfo(diary);

            mainDiaryDto.setDate(diary.getDiaryDate().toString());
            System.out.println("=================================================================" + mainDiaryDto.getDate());

            mainDiaryDto.setSentiment(diaryAnalyticsSentiment.getSentiment());
            mainDiaryDto.setMusic_title(musicInfo.getMusicTitle());
            mainDiaryDto.setMusic_artist(musicInfo.getMusicArtist());

            resultList.add(mainDiaryDto);
        }
        resultMap.put("message", "월별 일기 정보");
        resultMap.put("data", resultList);

        return new ResponseEntity<>(resultMap, HttpStatus.OK);

    }

    @GetMapping("/{id}/{startDate}/{endDate}/chart")
    public ResponseEntity<Map<String, Object>> MonthlyDiaryChart(@PathVariable String id,
                                                                 @PathVariable String startDate,
                                                                 @PathVariable String endDate) throws CustomException {
        Map<String, Object> resultMap = new HashMap();

        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);


        List<Diary> diaryList = mainService.getMontlyDiary(id, start, end);
        int MonthlyDiaryCount = diaryList.size();

        List<Float> sentimentList = new ArrayList<>();
        for(Diary diary : diaryList){
            int diaryId = diary.getDiaryId();
            DiaryAnalyticsSentiment diaryAnalyticsSentiment = diaryService.getDiaryAnalyticsSentiment(diaryId);

            sentimentList.add(diaryAnalyticsSentiment.getSentiment());
        }

        return new ResponseEntity<>(resultMap, HttpStatus.OK);
    }

}