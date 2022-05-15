package com.example.backend.bookmark;

import lombok.*;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Getter
@Setter
@Builder

public class BookmarkedDto {
    static List<String> happyList;
    static List<String> joyList;
    static List<String> sosoList;
    static List<String> sadList;
    static List<String> angryList;

}