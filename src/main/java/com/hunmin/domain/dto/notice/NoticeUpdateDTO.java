package com.hunmin.domain.dto.notice;

import com.hunmin.domain.entity.Member;
import com.hunmin.domain.entity.Notice;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@Setter
public class NoticeUpdateDTO {

    private Long noticeId;
    private Member member;
    private String title;
    private String content;

}