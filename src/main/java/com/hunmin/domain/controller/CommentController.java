package com.hunmin.domain.controller;

import com.hunmin.domain.dto.comment.CommentRequestDTO;
import com.hunmin.domain.dto.comment.CommentResponseDTO;
import com.hunmin.domain.dto.page.PageRequestDTO;
import com.hunmin.domain.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/board/{boardId}/comment")
public class CommentController {
    private final CommentService commentService;

    //댓글 등록
    @PostMapping
    public ResponseEntity<CommentResponseDTO> createComment(@PathVariable Long boardId, @RequestBody CommentRequestDTO commentRequestDTO) {
        return ResponseEntity.ok(commentService.createComment(boardId, commentRequestDTO));
    }

    //대댓글 등록
    @PostMapping("/{commentId}")
    public ResponseEntity<CommentResponseDTO> createCommentChild(@PathVariable Long boardId, @PathVariable Long commentId, @RequestBody CommentRequestDTO commentRequestDTO) {
        return ResponseEntity.ok(commentService.createCommentChild(boardId, commentId, commentRequestDTO));
    }

    //댓글 수정
    @PutMapping("/{commentId}")
    public ResponseEntity<CommentResponseDTO> updateComment(@PathVariable Long boardId, @PathVariable Long commentId, @RequestBody CommentRequestDTO commentRequestDTO) {
        return ResponseEntity.ok(commentService.updateComment(commentId, commentRequestDTO));
    }

    //댓글 삭제
    @DeleteMapping("/{commentId}")
    public ResponseEntity<Map<String, String>> deleteComment(@PathVariable Long boardId, @PathVariable Long commentId) {
        commentService.deleteComment(commentId);

        return ResponseEntity.ok(Map.of("result", "success"));
    }

    //게시글 별 댓글 목록 조회
    @GetMapping
    public ResponseEntity<Page<CommentResponseDTO>> readCommentList(@PathVariable Long boardId,
                                                                    @RequestParam(value = "page", defaultValue = "1") int page,
                                                                    @RequestParam(value = "size", defaultValue = "5") int size) {
        PageRequestDTO pageRequestDTO = PageRequestDTO.builder().page(page).size(size).build();

        return ResponseEntity.ok(commentService.readCommentList(boardId, pageRequestDTO));
    }
}
