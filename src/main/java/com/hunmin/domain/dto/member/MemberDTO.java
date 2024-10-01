package com.hunmin.domain.dto.member;

import com.hunmin.domain.entity.MemberRole;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class MemberDTO {
    private String email;
    private String password;
    private String nickname;
    private String country;
    private String level;

}
