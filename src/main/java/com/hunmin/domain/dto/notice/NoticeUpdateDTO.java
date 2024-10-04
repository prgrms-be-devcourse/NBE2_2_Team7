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
/*
RequestDTO와 인스턴스가 같긴 한데 용도는 다르니 혹시몰라서 ..
 */

    @NotEmpty(message = "제목을 입력하세요")
    private String title;
    @NotEmpty(message = "내용을 입력하세요")
    private String content;



}