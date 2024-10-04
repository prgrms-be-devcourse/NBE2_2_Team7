package com.hunmin.domain.dto.notice;

import com.hunmin.domain.entity.Notice;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class NoticeResponseDTO {

    private Long noticeId;
    private Long memberId;
    private String title;
    private String content;
    private String nickname;
    private LocalDate createdAt;


    public NoticeResponseDTO(Notice notice) {
        this.noticeId = notice.getNoticeId();
        this.memberId = notice.getMember().getMemberId();
        this.title = notice.getTitle();
        this.content = notice.getContent();
        this.nickname = notice.getMember().getNickname();
        this.createdAt = LocalDate.now();
    }
}
