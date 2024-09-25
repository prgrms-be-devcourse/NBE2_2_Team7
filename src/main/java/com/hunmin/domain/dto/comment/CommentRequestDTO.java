package com.hunmin.domain.dto.comment;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CommentRequestDTO {
    private Long commentId;
    private Long boardId;
    private Long memberId;
    private String content;
}
