package com.hunmin.domain.dto.board;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BoardRequestDTO {
    private Long boardId;
    private Long memberId;
    private String title;
    private String content;
    private String location;
    private Double latitude;
    private Double longitude;
}
