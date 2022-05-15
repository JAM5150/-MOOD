package com.example.backend.member;

public class MemberAdapter {
    public static MemberDto entityToDto(UserProfile userProfile, UserAuth userAuth){
        return new MemberDto.MemberDtoBuilder()
                .uid(userAuth.getUid())
                .email(userAuth.getEmail())
                .emailCompany(userAuth.getEmailCompany())
                .filePath(userProfile.getFilePath())
                .nickname(userProfile.getNickname())
                .hasBookmarked(userProfile.isHasBookmarked())
                .build();
    }
}
