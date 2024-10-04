package com.hunmin.domain.controller;

import com.hunmin.domain.dto.notice.*;

import com.hunmin.domain.service.NoticeService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notices")
@RequiredArgsConstructor
@Tag(name = "공지사항", description = "공지사항 CRUD")
public class NoticeController {

    private final NoticeService noticeService;

    @GetMapping("/list/{page}")
    @Operation(summary = "페이지 조회", description = "공지사항을 페이지로 조회할때 사용하는 API")
    public ResponseEntity<List<NoticeResponseDTO>> getNoticeList(@Validated NoticePageRequestDTO noticePageRequestDTO, @PathVariable int page) {
        noticePageRequestDTO.setPage(page); // 페이지 번호 설정
        List<NoticeResponseDTO> noticeList = noticeService.getAllNotices(noticePageRequestDTO);
        return ResponseEntity.ok(noticeList);
    }

    @GetMapping("/{noticeId}")
    @Operation(summary = "공지 조회", description = "공지사항을 조회할때 사용하는 API")
    public ResponseEntity<NoticeResponseDTO> getNotice(@PathVariable Long noticeId) {
        NoticeResponseDTO notice = noticeService.getNoticeById(noticeId);
        return ResponseEntity.ok(notice);
    }

    @PostMapping
    @Operation(summary = "공지 등록", description = "공지사항을 등록할때 사용하는 API")
    public ResponseEntity<NoticeResponseDTO> createNotice(@Validated @RequestBody NoticeRequestDTO noticeRequestDTO, @AuthenticationPrincipal UserDetails username) {
        NoticeResponseDTO notice = noticeService.createNotice(noticeRequestDTO, username.getUsername()); //이메일 반환
        return ResponseEntity.ok(notice);
    }

    @PutMapping("/{noticeId}")
    @Operation(summary = "공지 수정", description = "공지사항을 수정할때 사용하는 API")
    public ResponseEntity<NoticeResponseDTO> updateNotice(@PathVariable Long noticeId, @Validated @RequestBody NoticeUpdateDTO noticeUpdateDTO, @AuthenticationPrincipal UserDetails username) {
        NoticeResponseDTO notice = noticeService.updateNotice(noticeUpdateDTO, username.getUsername(), noticeId);
        return ResponseEntity.ok(notice);
    }

    @DeleteMapping("/{noticeId}")
    @Operation(summary = "공지 삭제", description = "공지사항을 삭제할때 사용하는 API")
    public ResponseEntity<Map<String,String>> deleteNotice(@PathVariable Long noticeId, @Validated @RequestBody NoticeDeleteDTO noticeDeleteDTO, @AuthenticationPrincipal UserDetails username) {
        noticeDeleteDTO.setNoticeId(noticeId);
        boolean result = noticeService.deleteNotice(noticeDeleteDTO, username.getUsername());
        if (result) {
            return ResponseEntity.ok(Map.of("result","success"));
        }else {
            return ResponseEntity.ok(Map.of("result","fail"));
        }

    }

}
