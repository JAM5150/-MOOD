package com.example.backend.diary.Diary;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import com.example.backend.error.CustomException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import com.example.backend.music.MusicInfo;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@CrossOrigin
@AllArgsConstructor
@RequestMapping("/diary")
public class DiaryController {

    private DiaryService diarySerivce;

    @GetMapping("/{id}/{date}")
    public ResponseEntity<Map<String, Object>> ReadDiary(
            @PathVariable String id,
            @PathVariable String date
    ){
        LocalDate localdate = LocalDate.parse(date,DateTimeFormatter.ISO_DATE);

        Map<String, Object> resultmap= new HashMap<String, Object>();
        DiaryDto currentDiary=diarySerivce.getDiaryByUidAndDiaryDate(id, localdate);
        //일기, 일기감정분석, 일기감성분석
        DiaryDto recommendDiary=diarySerivce.getSimilarDiary(id,currentDiary);


        resultmap.put("status", HttpStatus.OK);
        resultmap.put("message", "일기 정보");
        resultmap.put("diaryInfo",currentDiary);
        resultmap.put("recommendDiary", recommendDiary);



        return new ResponseEntity<>(resultmap,HttpStatus.OK);
    }

    @PostMapping("/{id}")
    public ResponseEntity<Map<String, Object>> WriteDiary(
            @PathVariable String id,
            @RequestParam String content,
            @RequestParam String date
    ) throws CustomException {
        LocalDate localdate= LocalDate.parse(date,DateTimeFormatter.ISO_DATE);
        Map<String, Object> resultMap=new HashMap<>();
        int result=diarySerivce.WriteDiary(id, content, localdate);


        resultMap.put("status", HttpStatus.OK);
        resultMap.put("meesage", "success");
        resultMap.put("diary_id", result);
        return new ResponseEntity<>(resultMap,HttpStatus.OK);
    }

    @PostMapping("/{diary_id}/edit")
    public ResponseEntity<Map<String , Object>> modifyDiary(
            @PathVariable int diary_id,
            @RequestParam String content,
            @RequestParam String date
    ){
        LocalDate localdate= LocalDate.parse(date,DateTimeFormatter.ISO_DATE);
        Map<String , Object> resultMap=new HashMap<>();
        int result=diarySerivce.modifyDiary(diary_id, content,localdate);
        resultMap.put("status", HttpStatus.OK);
        resultMap.put("meesage", "success");
        resultMap.put("diary_id",result);

        return new ResponseEntity<>(resultMap,HttpStatus.OK);
    }

    @DeleteMapping("/{diary_id}/delete")
    public ResponseEntity<Map<String , Object>> deleteDiary(
            @PathVariable int diary_id
    ){
        Map<String , Object> resultMap=new HashMap<>();
        diarySerivce.deleteDiary(diary_id);

        resultMap.put("status", HttpStatus.OK);
        resultMap.put("meesage", "success");


        return new ResponseEntity<>(resultMap,HttpStatus.OK);
    }

    @GetMapping("/music/{diary_id}/{uid}")
    public ResponseEntity<Map<String , Object>> getMusicList(
            @PathVariable int diary_id,
            @PathVariable String uid
    ){
        Map<String , Object> resultMap=new HashMap<>();
        MusicInfo[] result=new MusicInfo[5];
        result=diarySerivce.getMusicList(diary_id, uid);


        resultMap.put("status", HttpStatus.OK);
        resultMap.put("meesage", "success");
        resultMap.put("result", result);

        return new ResponseEntity<>(resultMap,HttpStatus.OK);
    }



}
