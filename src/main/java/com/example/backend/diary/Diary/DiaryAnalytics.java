package com.example.backend.diary.Diary;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DiaryAnalytics {

    @Id
    private int diaryId;

    @Column(nullable = false)
    private float happy;

    @Column(nullable =  false)
    private float joy;

    @Column(nullable = false)
    private float soso;

    @Column(nullable =  false)
    private float sad;

    @Column(nullable =  false)
    private float angry;

    @OneToOne
    @MapsId
    @JoinColumn(name = "diaryId")
    private Diary diary;

    public void updaetemotion(float[] emotion) {
        this.happy=emotion[0];
        this.joy=emotion[1];
        this.soso=emotion[2];
        this.sad=emotion[3];
        this.angry=emotion[4];
    }
}