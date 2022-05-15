package com.example.backend.diary.Diary;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.example.backend.member.UserAuth;
import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.example.backend.music.MusicInfo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Diary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int diaryId;


    @ManyToOne
    @JoinColumn(name = "uid")
    private UserAuth userAuth;

    @Column(nullable = false)
    private LocalDate diaryDate;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    @JsonIgnore
    @CreationTimestamp
    private LocalDate regDate;

    @Column( nullable =false)
    private boolean isDeleted;


    public void deletediary() {
        this.isDeleted=true;
    }
    public void updatecontent(String content) {
        this.content=content;
    }

}