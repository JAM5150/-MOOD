package com.example.backend.bookmark;


import ch.qos.logback.core.net.SyslogOutputStream;
import com.example.backend.error.CustomException;
import com.example.backend.error.ErrorCode;
import com.example.backend.member.emotion.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.springframework.data.jpa.provider.HibernateUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Slf4j
@RestController
@AllArgsConstructor
@CrossOrigin
@RequestMapping("/bookmark")
public class BookmarkController {
    private BookmarkService bookmarkService;
    private UserEmotionService userEmotionService;

    @GetMapping()
    public ResponseEntity<Map<String, Object>> BookmarkStart() throws CustomException {
        Map<String, Object> resultMap = new HashMap<>();
        List<Bookmark> bookmarkList =BookmarkService.getAllBookmark();

        resultMap.put("message", "북마크 리스트");
        resultMap.put("Bookmark", bookmarkList);

        return new ResponseEntity<>(resultMap, HttpStatus.OK);
    }

    @PostMapping("/{id}")
    public ResponseEntity<Map<String, Object>> BookmarkSave(@PathVariable("id") String id,
                                                          BookmarkedDto bookmarkDto) throws CustomException {
        Map<String, Object> resultMap = new HashMap<>();

        //시작전에 이미 설문조사를 진행한 사용자인지 체크
        Optional<List<UserEmotion>> userEmotionList = userEmotionService.getUserEmotion(id);
        if(userEmotionList.isPresent() && userEmotionList.get().size()==12){
            throw new CustomException(ErrorCode.ALREADY_BOOKMARKED);
        }

        HashMap<String, ArrayList<String>> map = new HashMap<>();

        List<String> happyList = BookmarkedDto.happyList;
        List<String> joyList = BookmarkedDto.joyList;
        List<String> sosoList = BookmarkedDto.sosoList;
        List<String> sadList = BookmarkedDto.sadList;
        List<String> angryList = BookmarkedDto.angryList;

        for(String s : happyList){
            if(map.get(s) == null){
                ArrayList<String> list = new ArrayList<>();
                map.put(s, list);
            }
            map.get(s).add("happy");
        }
        for(String s : joyList){
            if(map.get(s) == null){
                ArrayList<String> list = new ArrayList<>();
                map.put(s, list);
            }
            map.get(s).add("joy");
        }
        for(String s : sosoList){
            if(map.get(s) == null){
                ArrayList<String> list = new ArrayList<>();
                map.put(s, list);
            }
            map.get(s).add("soso");
        }
        for(String s : sadList){
            if(map.get(s) == null){
                ArrayList<String> list = new ArrayList<>();
                map.put(s, list);
            }
            map.get(s).add("sad");
        }
        for(String s : angryList){
            if(map.get(s) == null){
                ArrayList<String> list = new ArrayList<>();
                map.put(s, list);
            }
            map.get(s).add("angry");
        }

        for(Genre genre : Genre.values()){
            float happy = 0;
            float joy = 0;
            float soso = 0;
            float sad = 0;
            float angry = 0;

            BookmarkService.initialBookmark(genre, id, happy, joy, soso, sad, angry);
        }

        for(String s : map.keySet()){
            Genre genre= Genre .valueOf(s);
            float happy = 0;
            float joy = 0;
            float soso = 0;
            float sad = 0;
            float angry = 0;

            for(String emotion : map.get(s)){
                if(emotion.equals("happy")){
                    happy = 10;
                }
                if(emotion.equals("joy")){
                    joy = 10;
                }
                if(emotion.equals("soso")){
                    soso = 10;
                }
                if(emotion.equals("sad")){
                    sad = 10;
                }
                if(emotion.equals("angry")){
                    angry = 10;
                }
            }
            BookmarkService.saveBookmark(genre, id, happy, joy, soso, sad, angry);
        }

        resultMap.put("message", "북마크 결과 저장 성공");

        return new ResponseEntity<>(resultMap, HttpStatus.OK);
    }

    @DeleteMapping ("/reset/{id}")
    public ResponseEntity<Map<String, Object>> resetBookmark(@PathVariable String id) throws CustomException {
        Map<String, Object> resultMap = new HashMap<>();
        BookmarkService.resetBookmark(id);

        resultMap.put("message", "북마크 초기화 성공");

        return new ResponseEntity<>(resultMap, HttpStatus.OK);
    }
}