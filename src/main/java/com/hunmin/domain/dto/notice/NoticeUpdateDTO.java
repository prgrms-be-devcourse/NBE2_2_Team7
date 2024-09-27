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
    private Long memberId;
    @NotEmpty(message = "제목을 입력하세요")
    private String title;
    @NotEmpty(message = "내용을 입력하세요")
    private String content;



}