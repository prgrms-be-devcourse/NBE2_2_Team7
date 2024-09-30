package com.hunmin.domain.dto.member;

import com.hunmin.domain.entity.MemberRole;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class MemberDTO {
    private Long memberId;
    private String password;
    private String email;
    private String nickname;
    private String country;
    private String level;
    private String image;
    private MemberRole memberRole;

//    public MemberDTO() {}
//
//    public MemberDTO(Member member) {
//        this.memberId = member.getMemberId();
//        this.email = member.getEmail();
//        this.password = member.getPassword();
//        this.nickname = member.getNickname();
//        this.country = member.getCountry();
//        this.level = member.getLevel();
//        this.image = member.getImage();
//        this.memberRole = member.getMemberRole();
//    }
}
