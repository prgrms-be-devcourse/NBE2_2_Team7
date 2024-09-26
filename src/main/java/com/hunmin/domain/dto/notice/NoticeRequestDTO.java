package com.hunmin.domain.dto.notice;

import com.hunmin.domain.entity.BaseTimeEntity;
import com.hunmin.domain.entity.Member;
import com.hunmin.domain.entity.Notice;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@Setter
public class NoticeRequestDTO {

    @NotNull
    private Member member;
    @NotEmpty(message = "제목을 입력하세요")
    private String title;
    @NotEmpty(message = "내용을 입력하세요")
    private String content;

    public Notice toEntity(){
        return Notice.builder()
                .member(member)
                .title(title)
                .content(content)
                .build();
    }
}