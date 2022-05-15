package com.example.backend.bookmark;

import com.example.backend.member.emotion.Genre;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Bookmark {
    @Id
    private String genre;

    @Column(nullable = false)
    private String videoId;

    @Column(nullable = false, length = 32)
    private String musicTitle;

    @Column(nullable = false, length = 32)
    private String musicArtist;
}