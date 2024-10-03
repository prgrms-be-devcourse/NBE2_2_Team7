package com.hunmin.domain.controller;

import com.hunmin.domain.dto.member.MemberDTO;
import com.hunmin.domain.service.MemberService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

// 회원 가입, 회원 정보 수정 컨트롤러 구현
@RestController
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/uploads")
    public ResponseEntity<String> uploadImage(@RequestParam("image") MultipartFile image) {
        try {
            String imageUrl = memberService.uploadImage(image);
            return ResponseEntity.ok(imageUrl);  // 이미지 URL 반환
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("이미지 업로드 실패");
        }
    }

    @PostMapping("/api/members/register")
    public ResponseEntity<String> registerProcess(@RequestBody MemberDTO memberDTO) {
        try {
            memberService.registerProcess(memberDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body("회원 가입 완료");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("회원 가입 실패");
        }
    }

    @PutMapping("/api/members/{memberId}")
    public ResponseEntity<?> updateMember(@PathVariable Long memberId, @RequestBody MemberDTO memberDTO) {
        memberService.updateMember(memberId, memberDTO);
        return ResponseEntity.ok().build();
    }

}
