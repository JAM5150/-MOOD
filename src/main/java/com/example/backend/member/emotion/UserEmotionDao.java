package com.example.backend.member.emotion;

import com.example.backend.member.UserAuth;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public interface UserEmotionDao extends JpaRepository<UserEmotion, UserEmotionPK> {

    //where id={id} and is_deleted=false
    Optional<List<UserEmotion>> getUserEmotionByUserAuth(UserAuth userAuth);
    UserEmotion getUserEmotionByUserAuthAndEmbGenre(UserAuth userAuth,UserEmotionPK embGenre);

    @Transactional
    void deleteUserEmotionByUserAuth(UserAuth userAuth);
}