package com.hunmin.domain.dto.board;

import com.hunmin.domain.dto.comment.CommentResponseDTO;
import com.hunmin.domain.entity.Board;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class BoardResponseDTO {
    private Long boardId;
    private String title;
    private String nickname;
    private String content;
    private String location;
    private Double latitude;
    private Double longitude;
    private List<String> imageUrls;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<CommentResponseDTO> comments;

    public BoardResponseDTO(Board board) {
        this.boardId = board.getBoardId();
        this.title = board.getTitle();
        this.nickname = board.getNickname();
        this.content = board.getContent();
        this.location = board.getLocation();
        this.latitude = board.getLatitude();
        this.longitude = board.getLongitude();
        this.imageUrls = board.getImageUrls();
        this.createdAt = board.getCreatedAt();
        this.updatedAt = board.getUpdatedAt();
        if (board.getComments() != null) {
            this.comments = new ArrayList<>();
            board.getComments().forEach(comment -> this.comments.add(new CommentResponseDTO(comment)));
        } else {
            this.comments = new ArrayList<>();
        }
    }
}
