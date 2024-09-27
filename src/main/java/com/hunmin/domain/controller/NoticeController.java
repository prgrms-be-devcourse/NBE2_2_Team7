package com.hunmin.domain.controller;

import com.hunmin.domain.dto.notice.*;

import com.hunmin.domain.service.NoticeService;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notices")
@RequiredArgsConstructor
public class NoticeController {

    private final NoticeService noticeService;

    @GetMapping("/list/{page}")
    public ResponseEntity<List<NoticeResponseDTO>> getNoticeList(@Validated NoticePageRequestDTO noticePageRequestDTO, @PathVariable int page) {
        noticePageRequestDTO.setPage(page); // 페이지 번호 설정
        List<NoticeResponseDTO> noticeList = noticeService.getAllNotices(noticePageRequestDTO);
        return ResponseEntity.ok(noticeList);
    }

    @GetMapping("/{noticeId}")
    public ResponseEntity<NoticeResponseDTO> getNotice(@PathVariable Long noticeId) {
        NoticeResponseDTO notice = noticeService.getNoticeById(noticeId);
        return ResponseEntity.ok(notice);
    }

    @PostMapping
    public ResponseEntity<NoticeResponseDTO> createNotice(@Validated @RequestBody NoticeRequestDTO noticeRequestDTO) {
        NoticeResponseDTO notice = noticeService.createNotice(noticeRequestDTO);
        return ResponseEntity.ok(notice);
    }

    @PutMapping("/{noticeId}")
    public ResponseEntity<NoticeResponseDTO> updateNotice(@PathVariable Long noticeId, @Validated @RequestBody NoticeUpdateDTO noticeUpdateDTO) {
        noticeUpdateDTO.setNoticeId(noticeId);
        NoticeResponseDTO notice = noticeService.updateNotice(noticeUpdateDTO);
        return ResponseEntity.ok(notice);
    }

    @DeleteMapping("/{noticeId}")
    public ResponseEntity<Map<String,String>> deleteNotice(@PathVariable Long noticeId, @Validated @RequestBody NoticeDeleteDTO noticeDeleteDTO) {
        noticeDeleteDTO.setNoticeId(noticeId);
        boolean result = noticeService.deleteNotice(noticeDeleteDTO);
        if (result) {
            return ResponseEntity.ok(Map.of("result","success"));
        }else {
            return ResponseEntity.ok(Map.of("result","fail"));
        }

    }

}
