package com.example.backend.member;


import com.example.backend.amazonS3.S3Uploader;
import com.example.backend.error.CustomException;
import com.example.backend.error.ErrorCode;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Slf4j
@AllArgsConstructor
@Service
public class MemberService {
    private static MemberAuthDao memberAuthDao;
    private MemberProfileDao memberProfileDao;
    private S3Uploader s3Uploader;

    public Optional<MemberDto> getMemberByEmail(String email){
        Optional<UserAuth> userAuth = memberAuthDao.getUserAuthByEmail(email);
        Optional<MemberDto> memberDto = Optional.ofNullable(null);

        if(userAuth.isPresent()){
            Optional<UserProfile> userProfile = memberProfileDao.findById(userAuth.get().getUid());
            memberDto = Optional.of(MemberAdapter.entityToDto(userProfile.get(), userAuth.get()));
        }
        return memberDto;
    }

    public void joinMember(String companyName, String email) {
        long randomNum = (long)(Math.random() * (long)Math.pow(10, 12));
        String uid = companyName.substring(0, 1).toUpperCase() + Long.toString(randomNum);

        UserAuth userAuth = UserAuth.builder()
                .uid(uid)
                .email(email)
                .emailCompany(companyName)
                .build();

        UserAuth mappingUserAuth = memberAuthDao.save(userAuth);

        UserProfile userProfile = UserProfile.builder()
                .filePath("https://mlog-image-bucket.s3.ap-northeast-2.amazonaws.com/profile_default.png")
                .nickname("default")
                .hasBookmarked(false)
                .userAuth(mappingUserAuth)
                .build();

        memberProfileDao.save(userProfile);
    }

    public MemberDto updateMember(MemberDto memberDto) throws IOException, CustomException {
        UserAuth userAuth = memberAuthDao.findById(memberDto.getUid())
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        if(memberDto.getImage() != null){
            memberDto.setFilePath(insertImage(memberDto.getImage()));
        }

        UserProfile userProfile = UserProfile.builder()
                .uid(memberDto.getUid())
                .filePath(memberDto.getFilePath())
                .nickname(memberDto.getNickname())
                .hasBookmarked(memberDto.isHasBookmarked())
                .build();

        memberProfileDao.save(userProfile);

        memberDto.setImage(null);
        return memberDto;
    }

    private String insertImage(MultipartFile image) throws IOException {
        return s3Uploader.upload(image, "static");
    }

    public static void deleteUser(String uid) throws CustomException {
        memberAuthDao.findById(uid)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        memberAuthDao.deleteUserAuthByUid(uid);
    }
}