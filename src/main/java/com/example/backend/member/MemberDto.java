package com.example.backend.member;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberDto {
    private String uid;
    private String email;
    private String emailCompany;
    private MultipartFile image;
    private String filePath;
    private String nickname;
    private boolean hasBookmarked;
}
