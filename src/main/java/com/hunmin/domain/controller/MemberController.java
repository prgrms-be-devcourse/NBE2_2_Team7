package com.hunmin.domain.controller;

import com.hunmin.domain.dto.member.MemberDTO;
import com.hunmin.domain.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    // 특정 멤버를 memeberId로 조회
    @GetMapping("/{memberId}")
    public ResponseEntity<MemberDTO> getMemberById(@PathVariable Long memberId) {
        MemberDTO memberDTO = memberService.findById(memberId);
        return ResponseEntity.ok(memberDTO);
    }

    // 새로운 멤버 등록
    @PostMapping
    public ResponseEntity<MemberDTO> createMember(@RequestBody MemberDTO memberDTO) {
        MemberDTO createdMember = memberService.createMember(memberDTO);
        return ResponseEntity.ok(createdMember);
    }

    // 멤버 정보 수정
    @PutMapping("/{memberId}")
    public ResponseEntity<MemberDTO> updateMember(@PathVariable Long memberId, @RequestBody MemberDTO memberDTO) {
        MemberDTO updateMember = memberService.updateMember(memberId, memberDTO);
        return ResponseEntity.ok().body(memberDTO);
    }

    // 멤버 삭제
    @DeleteMapping("/{memberId}")
    public ResponseEntity<Void> deleteMember(@PathVariable Long memberId) {
        memberService.deleteMember(memberId);
        return ResponseEntity.noContent().build();
    }
}
