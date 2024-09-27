package com.hunmin.domain.dto.member;

import com.hunmin.domain.entity.Member;
import com.hunmin.domain.entity.MemberRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberDTO {

    private Long memberId;
    private String password;
    private String email;
    private String nickname;
    private String country;
    private String level;
    private String image;
    private MemberRole memberRole;


    public MemberDTO(Member member) {
        this.memberId = member.getMemberId();
        this.email = member.getEmail();
        this.password = member.getPassword();
        this.nickname = member.getNickname();
        this.country = member.getCountry();
        this.level = member.getLevel();
        this.image = member.getImage();
        this.memberRole = member.getMemberRole();
    }
}
