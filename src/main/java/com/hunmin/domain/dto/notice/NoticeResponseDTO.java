package com.hunmin.domain.dto.notice;

import com.hunmin.domain.entity.Member;
import com.hunmin.domain.entity.Notice;
import lombok.Getter;

@Getter
public class NoticeResponseDTO {

    private Long noticeId;
    private Member member;
    private String title;
    private String content;

    public NoticeResponseDTO(Notice notice) {
        this.noticeId = notice.getNoticeId();
        this.member = notice.getMember();
        this.title = notice.getTitle();
        this.content = notice.getContent();
    }
}
