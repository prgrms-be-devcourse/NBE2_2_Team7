package com.hunmin.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@Setter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseTimeEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false)
    private String country;

    @Column(nullable = false)
    private String level;

    private String image;

    @Enumerated(EnumType.STRING)
    private MemberRole memberRole;

    public Member(Long memberId, String nickname) {
        this.memberId = memberId;
        this.nickname = nickname;
    }
}
