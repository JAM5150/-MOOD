package com.example.backend.member.emotion;


import  com.example.backend.member.UserAuth;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserEmotion {

    @EmbeddedId
    private UserEmotionPK embGenre;

    @ManyToOne
    @MapsId("uid") //UserEmotionPK
    @JoinColumn(name="uid") //UserAuth
    private UserAuth userAuth;

    @Column(nullable = false)
    @ColumnDefault("0")
    private float happy;

    @Column(nullable = false)
    @ColumnDefault("0")
    private float joy;

    @Column(nullable = false)
    @ColumnDefault("0")
    private float soso;

    @Column(nullable = false)
    @ColumnDefault("0")
    private float sad;

    @Column(nullable = false)
    @ColumnDefault("0")
    private float angry;

    public void updateUserEmotion(
            float happy,
            float joy,
            float soso,
            float sad,
            float angry) {
        this.angry=angry;
        this.soso=soso;
        this.happy=happy;
        this.sad=sad;
        this.joy=joy;
    }

}
