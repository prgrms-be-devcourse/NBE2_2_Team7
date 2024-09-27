package com.hunmin.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@ToString
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String nickname;

    @Column(nullable = false)
    private String country;

    @Column(nullable = false)
    private String level;

    private String image;

    @Enumerated(EnumType.STRING)
    private MemberRole memberRole;

    public void changePassword(String encode) {
        this.password = encode;
    }

    public void changeNickname(String nickname) {
        this.nickname = nickname;
    }

    public void changeCountry(String country) {
        this.country = country;
    }

    public void changeLevel(String level) {
        this.level = level;
    }

    public void changeImage(String image) {
        this.image = image;
    }
}
