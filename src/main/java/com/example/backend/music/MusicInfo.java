package com.example.backend.music;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MusicInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int mid;

    @Column(nullable = false, length = 255)
    private String musicTitle;

    @Column(nullable = false, length = 255)
    private String musicArtist;

    @Column(nullable = false, length = 255)
    private String videoId;

    @Column(nullable = false, length = 20)
    private String musicGenre;

    @Column(nullable = false, length = 255)
    private String filePath;
}
