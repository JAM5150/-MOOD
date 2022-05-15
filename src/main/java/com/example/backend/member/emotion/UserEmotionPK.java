package com.example.backend.member.emotion;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.io.Serializable;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class UserEmotionPK implements Serializable {

    private String uid;

    @Enumerated(EnumType.STRING)
    private Genre genre;

    public Genre getGenre() {
        return this.genre;
    }


}