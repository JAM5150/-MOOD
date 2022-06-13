package com.example.backend.dao.user;

import java.util.Optional;

import com.example.backend.model.user.User;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.JpaRepository;


@Configuration
public interface UserDao extends JpaRepository<User, String> {


    User getUserByEmail(String email);

    Optional<User> findUserByEmailAndPassword(String email, String password);
}