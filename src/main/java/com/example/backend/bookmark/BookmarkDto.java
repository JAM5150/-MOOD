package com.example.backend.bookmark;

import lombok.*;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class BookmarkDto {

    static List<String> happyList;
    List<String> happylList;
    static List<String> joyList;
    static List<String> sosoList;
    static List<String> sadList;
    static List<String> angryList;
}
