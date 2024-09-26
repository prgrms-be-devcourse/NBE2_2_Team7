package com.hunmin.domain.dto.notice;

import com.hunmin.domain.entity.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@Setter
public class NoticeDeleteDTO {

    private Long noticeId;
    private Member member;

}