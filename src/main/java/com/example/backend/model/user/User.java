package com.example.backend.model.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)

public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String uid;

    @JsonIgnore
    private String password;
    private String email;

    @Column(insertable = false, updatable = false)
    private LocalDateTime createDate;
}
