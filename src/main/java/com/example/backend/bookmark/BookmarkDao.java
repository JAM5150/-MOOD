package com.example.backend.bookmark;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BookmarkDao extends JpaRepository<Bookmark, String> {

}