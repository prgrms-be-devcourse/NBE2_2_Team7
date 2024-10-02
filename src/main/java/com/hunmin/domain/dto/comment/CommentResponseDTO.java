package com.hunmin.domain.dto.comment;

import com.hunmin.domain.entity.Comment;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class CommentResponseDTO {
    private Long commentId;
    private Long boardId;
    private Long memberId;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String nickname;
    private List<CommentResponseDTO> children;

    public CommentResponseDTO(Comment comment) {
        this.commentId = comment.getCommentId();
        this.boardId = comment.getBoard().getBoardId();
        this.memberId = comment.getMember().getMemberId();
        this.content = comment.getContent();
        this.createdAt = comment.getCreatedAt();
        this.updatedAt = comment.getUpdatedAt();
        this.nickname = comment.getMember().getNickname();
        this.children = (comment.getChildren() != null) ? comment.getChildren().stream()
                .sorted(Comparator.comparing(Comment::getCommentId))
                .map(CommentResponseDTO::new)
                .collect(Collectors.toList()) : new ArrayList<>();
    }
}
