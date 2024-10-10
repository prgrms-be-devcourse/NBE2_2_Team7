package com.hunmin.domain.dto.notice;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.hunmin.domain.entity.Member;
import com.hunmin.domain.entity.Notice;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class NoticeRequestDTO {

    @NotEmpty(message = "제목을 입력하세요")
    private String title;
    @NotEmpty(message = "내용을 입력하세요")
    private String content;

    public Notice toEntity(){
        return Notice.builder()
                .title(title)
                .content(content)
                .build();
    }

    public Notice toEntity(Member member){
        return Notice.builder()
                .member(member)
                .title(title)
                .content(content)
                .build();
    }
}