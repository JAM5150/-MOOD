package com.example.backend.member.emotion;


import com.example.backend.error.CustomException;
import com.example.backend.error.ErrorCode;
import com.example.backend.member.MemberAuthDao;
import com.example.backend.member.UserAuth;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@AllArgsConstructor
@Service
public class UserEmotionService {
    private UserEmotionDao userEmotionDao;
    private MemberAuthDao memberAuthDao;

    public Optional<List<UserEmotion>> getUserEmotion(String uid){
        UserAuth userAuth = MemberAuthDao.getUserAuthByUid(uid);
        Optional<List<UserEmotion>> userEmotion = userEmotionDao.getUserEmotionByUserAuth(userAuth);

        return userEmotion;
    }

}