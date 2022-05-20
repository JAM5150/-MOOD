package com.example.backend.profile;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import  com.example.backend.diary.Diary.DiaryAnalyticsSentiment;
import  com.example.backend.diary.Diary.DiaryDto;
import  com.example.backend.diary.Diary.DiaryService;
import com.example.backend.music.MusicDto;
import com.example.backend.music.MusicInfo;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@CrossOrigin
@AllArgsConstructor
@RequestMapping("/profile")
public class ProfileController {
    private ProfileService profileService;

    @GetMapping("/{id}/playlist")
    public ResponseEntity<Map<String, Object>> getMyPlayList(@PathVariable String id){
        Map<String, Object> resultmap= new HashMap<String, Object>();
        List<MusicDto> MusicInfoList=profileService.getMyPlayList(id);

        resultmap.put("status", HttpStatus.OK);
        resultmap.put("message", "플레이리스트 정보");
        resultmap.put("MusicInfoList", MusicInfoList);

        return new ResponseEntity<>(resultmap,HttpStatus.OK);
    }
    @GetMapping("{id}/diary")
    public ResponseEntity<Map<String, Object>> getMyDiary(@PathVariable String id){
        Map<String, Object> resultmap= new HashMap<String, Object>();
        List<DiaryDto> DiaryList=profileService.getMyDiary(id);

        resultmap.put("status", HttpStatus.OK);
        resultmap.put("message", "일기 정보");
        resultmap.put("DiaryList", DiaryList);

        return new ResponseEntity<>(resultmap,HttpStatus.OK);
    }

    @GetMapping("{id}/report/diary")
    public ResponseEntity<Map<String, Object>> getMyreportdiary(
            @PathVariable String id,
            @RequestParam String start_date,
            @RequestParam String end_date
    ){
        LocalDate startDate = LocalDate.parse(start_date,DateTimeFormatter.ISO_DATE);
        LocalDate endDate = LocalDate.parse(end_date,DateTimeFormatter.ISO_DATE);
        Map<String, Object> resultmap= new HashMap<String, Object>();
        int answer=profileService.getMYReportDiary(id, startDate, endDate);
        resultmap.put("status", HttpStatus.OK);
        resultmap.put("message", "해당 날짜의 일기 확인");
        resultmap.put("count", answer);

        return new ResponseEntity<>(resultmap,HttpStatus.OK);
    }

}
