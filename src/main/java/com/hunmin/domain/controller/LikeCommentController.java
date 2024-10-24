package com.hunmin.domain.controller;

import com.hunmin.domain.repository.MemberRepository;
import com.hunmin.domain.service.LikeCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/likeComment")
@RequiredArgsConstructor
public class LikeCommentController {

    private final LikeCommentService likeCommentService;
    private final MemberRepository memberRepository;

    //좋아요 등록
    @PostMapping("/{commentId}")
    public ResponseEntity<String> createLikeComment(@PathVariable Long commentId, Authentication authentication) {
        Long memberId = memberRepository.findByEmail(authentication.getName()).getMemberId();
        likeCommentService.createLikeComment(memberId, commentId);
        return ResponseEntity.ok().build();
    }

    //좋아요 삭제
    @DeleteMapping("/{commentId}")
    public ResponseEntity<String> deleteLikeComment(@PathVariable Long commentId, Authentication authentication) {
        Long memberId = memberRepository.findByEmail(authentication.getName()).getMemberId();
        likeCommentService.deleteLikeComment(memberId, commentId);
        return ResponseEntity.ok().build();
    }

    //좋아요 여부 확인
    @GetMapping("/{commentId}/member/{memberId}")
    public ResponseEntity<Boolean> isLikedComment(@PathVariable Long commentId, @PathVariable Long memberId) {
        boolean isLikedComment = likeCommentService.isLikeComment(memberId, commentId);
        return ResponseEntity.ok(isLikedComment);
    }
}
