package com.example.backend.member;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

    @Entity
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public class UserProfile {
        @Id
        private String uid;

        @Column(nullable = false, length = 256)
        private String filePath;

        @Column(nullable = false, length = 12)
        private String nickname;

        @Column(nullable = false)
        private boolean hasBookmarked;

        @OneToOne
        @MapsId
        @JoinColumn(name = "uid")
        private UserAuth userAuth;

        public void setHasBookmarkedTrue() {
            this.hasBookmarked=true;
        }
        public void setHasBookmarkedFalse() {
            this.hasBookmarked=false;
        }
    }

