package com.hunmin.domain.controller;

import com.hunmin.domain.dto.member.MemberDTO;
import com.hunmin.domain.service.MemberService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

// 회원 가입, 회원 정보 수정 컨트롤러 구현
@RestController
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/api/members/register")
    public ResponseEntity<String> registerProcess(@RequestBody MemberDTO memberDTO) {
        memberService.registerProcess(memberDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body("회원 가입이 완료되었습니다.");
    }

    @PutMapping("/api/members/{memberId}")
    public ResponseEntity<?> updateMember(@PathVariable Long memberId, @RequestBody MemberDTO memberDTO) {
        memberService.updateMember(memberId, memberDTO);
        return ResponseEntity.ok().build();
    }

}
