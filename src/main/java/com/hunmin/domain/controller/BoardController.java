package com.hunmin.domain.controller;

import com.hunmin.domain.dto.board.BoardRequestDTO;
import com.hunmin.domain.dto.board.BoardResponseDTO;
import com.hunmin.domain.dto.page.PageRequestDTO;
import com.hunmin.domain.service.BoardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/board")
@Tag(name = "게시글", description = "게시글 CRUD")
public class BoardController {
    private final BoardService boardService;

    //게시글 이미지 첨부
    @PostMapping("/uploadImage")
    @Operation(summary = "게시글 이미지 등록", description = "게시글에 여러 이미지를 등록할 때 사용하는 API")
    public ResponseEntity<List<String>> uploadImages(@RequestParam("files") MultipartFile[] files) {
        List<String> imageUrls = new ArrayList<>();

        try {
            for (MultipartFile file : files) {
                String imageUrl = boardService.uploadImage(file);
                imageUrls.add(imageUrl);
            }
            return ResponseEntity.ok(imageUrls);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(List.of("Image upload failed"));
        }
    }

    //게시글 등록
    @PostMapping
    @Operation(summary = "게시글 등록", description = "게시글을 등록할 때 사용하는 API")
    public ResponseEntity<BoardResponseDTO> createBoard(@RequestBody BoardRequestDTO boardRequestDTO) {
        return ResponseEntity.ok(boardService.createBoard(boardRequestDTO));
    }

    //게시글 조회
    @GetMapping("/{boardId}")
    @Operation(summary = "게시글 조회", description = "게시글을 조회할 때 사용하는 API")
    public ResponseEntity<BoardResponseDTO> readBoard(@PathVariable Long boardId) {
        return ResponseEntity.ok(boardService.readBoard(boardId));
    }

    //게시글 수정
    @PutMapping("/{boardId}")
    @Operation(summary = "게시글 수정", description = "게시글을 수정할 때 사용하는 API")
    public ResponseEntity<BoardResponseDTO> updateBoard(@PathVariable Long boardId, @RequestBody BoardRequestDTO boardRequestDTO) {
        return ResponseEntity.ok(boardService.updateBoard(boardId, boardRequestDTO));
    }

    //게시글 삭제
    @DeleteMapping("/{boardId}")
    @Operation(summary = "게시글 삭제", description = "게시글을 삭제할 때 사용하는 API")
    public ResponseEntity<Map<String, String>> deleteBoard(@PathVariable Long boardId) {
        boardService.deleteBoard(boardId);
        return ResponseEntity.ok(Map.of("result", "success"));
    }

    //게시글 목록 조회
    @GetMapping
    @Operation(summary = "게시글 목록", description = "게시글 목록을 조회할 때 사용하는 API")
    public ResponseEntity<Page<BoardResponseDTO>> readBoardList(@RequestParam(value = "page", defaultValue = "1") int page,
                                                                @RequestParam(value = "size", defaultValue = "5") int size) {
        PageRequestDTO pageRequestDTO = PageRequestDTO.builder().page(page).size(size).build();
        return ResponseEntity.ok(boardService.readBoardList(pageRequestDTO));
    }

    //회원 별 작성글 목록 조회
    @GetMapping("/member/{memberId}")
    @Operation(summary = "회원 별 작성글 목록", description = "회원별 작성글 목록을 조회할 때 사용하는 API")
    public ResponseEntity<Page<BoardResponseDTO>> readBoardList(@PathVariable Long memberId,
                                                                @RequestParam(value = "page", defaultValue = "1") int page,
                                                                @RequestParam(value = "size", defaultValue = "10") int size) {
        PageRequestDTO pageRequestDTO = PageRequestDTO.builder().page(page).size(size).build();
        return ResponseEntity.ok(boardService.readBoardListByMember(memberId, pageRequestDTO));
    }
}
