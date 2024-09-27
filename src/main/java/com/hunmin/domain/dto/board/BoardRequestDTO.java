package com.hunmin.domain.dto.board;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardRequestDTO {
    @NotBlank
    private Long boardId;

    @NotBlank
    private Long memberId;

    @NotEmpty
    private String title;

    @NotEmpty
    private String content;

    private String location;
    private Double latitude;
    private Double longitude;
}
