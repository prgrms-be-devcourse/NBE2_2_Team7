package com.hunmin.domain.controller;

import com.hunmin.domain.dto.member.MemberDTO;
import com.hunmin.domain.service.MemberService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RegisterController {

    private final MemberService memberService;

    public RegisterController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/api/members/register")
    public String joinProcess(MemberDTO memberDTO) {
        memberService.joinProcess(memberDTO);
        return "ok";
    }
}
