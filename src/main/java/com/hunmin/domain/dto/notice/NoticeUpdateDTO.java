package com.hunmin.domain.dto.notice;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
/* memberId는 SecurityContext 에서 받아오고 noticeId는 path variable 에서 받아오는데 필요하지 않아서 다 무시
나머지 불필요한 JSON 데이터 무시*/
public class NoticeUpdateDTO {


    @NotEmpty(message = "제목을 입력하세요")
    private String title;
    @NotEmpty(message = "내용을 입력하세요")
    private String content;





}