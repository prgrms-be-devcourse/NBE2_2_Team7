package com.hunmin.domain.dto.member;

import com.hunmin.domain.entity.Member;
import com.hunmin.domain.entity.MemberLevel;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor
public class MemberDTO {
    private Long memberId;
    private String email;
    private String password;
    private String nickname;
    private String country;
    private MemberLevel level;
    private String image;

    public MemberDTO(Member member) {
        this.memberId = member.getMemberId();
        this.email = member.getEmail();
        this.password = member.getPassword();
        this.nickname = member.getNickname();
        this.country = member.getCountry();
        this.level = member.getLevel();
        this.image = member.getImage();
    }
}
