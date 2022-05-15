package com.example.backend.member;

import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.util.Optional;

public interface MemberAuthDao extends JpaRepository<UserAuth, String>{

    Optional<UserAuth> getUserAuthByEmail(String email);

    static UserAuth getUserAuthByUid(String uid) {
        return null;
    }

    @Transactional
    void deleteUserAuthByUid(String uid);
}