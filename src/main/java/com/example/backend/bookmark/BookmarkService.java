package com.example.backend.bookmark;
import com.example.backend.error.CustomException;
import com.example.backend.error.ErrorCode;
import com.example.backend.member.MemberAuthDao;
import com.example.backend.member.MemberProfileDao;
import com.example.backend.member.UserAuth;
import com.example.backend.member.UserProfile;
import com.example.backend.member.emotion.Genre;
import com.example.backend.member.emotion.UserEmotion;
import com.example.backend.member.emotion.UserEmotionDao;
import com.example.backend.member.emotion.UserEmotionPK;
import com.example.backend.model.user.User;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.lang.reflect.Member;
import java.util.List;
import java.util.Optional;

@Slf4j
@AllArgsConstructor
@Service

public class BookmarkService {
    private BookmarkDao bookmarkDao;
    private MemberAuthDao memberAuthDao;
    private MemberProfileDao memberProfileDao;
    private UserEmotionDao userEmotionDao;

    public List<Bookmark> getAllBookmark() throws CustomException {
        List<Bookmark> survey = BookmarkDao.findAll();
        if(survey.size()<12){
            throw new CustomException(ErrorCode.BOOKMARK_LIST_NOT_FOUND);
        }
        return survey;
    }
    public static void initialBookmark(Genre genre, String uid, float happy, float joy, float soso, float sad, float angry) {
        UserAuth userAuth = memberAuthDao.getUserAuthByUid(uid);
        UserEmotionPK embGenre = UserEmotionPK.builder()
                .genre(genre)
                .build();

        UserEmotion userEmotion = UserEmotion.builder()
                .embGenre(embGenre)
                .userAuth(userAuth)
                .happy(happy)
                .joy(joy)
                .soso(soso)
                .sad(sad)
                .angry(angry)
                .build();

        userEmotionDao.save(userEmotion);
    }
    public void saveSurvey(Genre genre, String uid, float happy, float joy, float soso, float sad, float angry){
        UserEmotionPK embGenre = UserEmotionPK.builder()
                .genre(genre)
                .uid(uid)
                .build();

        UserEmotion userEmotion = UserEmotion.builder()
                .embGenre(embGenre)
                .happy(happy)
                .joy(joy)
                .soso(soso)
                .sad(sad)
                .angry(angry)
                .build();

        userEmotionDao.save(userEmotion);

        //설문조사 여부 변경
        Optional<UserProfile> userProfile = memberProfileDao.findById(uid);
        userProfile.get().setHasBookmarkTrue();
        memberProfileDao.save(userProfile.get());
    }

    public void resetSurvey(String uid) throws CustomException {
        UserAuth userAuth = memberAuthDao.findById(uid)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        UserProfile userProfile = memberProfileDao.getUserProfileByUserAuth(userAuth);

        Optional<List<UserEmotion>> userEmotionList = userEmotionDao.getUserEmotionByUserAuth(userAuth);

        if(!userProfile.isHasBookmark() && !userEmotionList.isPresent()){
            throw new CustomException(ErrorCode.ALREADY_RESET_BOOKMARKED);
        }

        userEmotionDao.deleteUserEmotionByUserAuth(userAuth);

        userProfile.setHasBookmarkFalse();
        memberProfileDao.save(userProfile);

    }
}
