package com.hunmin.domain.dto.member;

import com.hunmin.domain.entity.Member;

import com.hunmin.domain.entity.MemberLevel;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberStatusDTO {
    private Long memberId;
    private String email;
    private String nickname;
    private String image;
    private int boardCount;
    private int commentCount;

    public MemberStatusDTO(Member member , int boardCount, int commentCount) {
        this.memberId = member.getMemberId();
        this.email = member.getEmail();
        this.nickname = member.getNickname();
        this.image = member.getImage();
        this.boardCount = boardCount;
        this.commentCount = commentCount;
    }
}