package com.example.backend.member;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserAuth {
    @Id
    private String uid;

    @Column(nullable = false, length = 32)
    private String email;

    @Column(nullable = false, length = 25)
    private String emailCompany;

//	@OneToOne(mappedBy = "userAuth", cascade = CascadeType.ALL)
//	@PrimaryKeyJoinColumn
//	private UserProfile userProfile;
}