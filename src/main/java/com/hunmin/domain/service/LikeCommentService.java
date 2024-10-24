package com.hunmin.domain.service;

import com.hunmin.domain.entity.Comment;
import com.hunmin.domain.entity.LikeComment;
import com.hunmin.domain.entity.Member;
import com.hunmin.domain.exception.CommentException;
import com.hunmin.domain.exception.LikeCommentException;
import com.hunmin.domain.exception.MemberException;
import com.hunmin.domain.repository.CommentRepository;
import com.hunmin.domain.repository.LikeCommentRepository;
import com.hunmin.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Log4j2
public class LikeCommentService {
    private final LikeCommentRepository likeCommentRepository;
    private final MemberRepository memberRepository;
    private final CommentRepository commentRepository;

    //좋아요 등록
    @Transactional
    public void createLikeComment(Long memberId, Long commentId) {
        Member member = memberRepository.findById(memberId).orElseThrow(MemberException.NOT_FOUND::get);
        Comment comment = commentRepository.findById(commentId).orElseThrow(CommentException.NOT_FOUND::get);

        likeCommentRepository.findByMemberAndComment(member, comment).ifPresentOrElse(
                likeComment -> {
                    throw LikeCommentException.NOT_CREATED.get();
                },
                () -> {
                    likeCommentRepository.save(LikeComment.builder().member(member).comment(comment).build());
                    comment.incrementLikeCount();
                    log.info("__!_!_@!)@_~_!~__!~_!_!~~!_");
                    log.info(comment.getLikeCount());
                }
        );
    }

    //좋아요 삭제
    @Transactional
    public void deleteLikeComment(Long memberId, Long commentId) {
        Member member = memberRepository.findById(memberId).orElseThrow(MemberException.NOT_FOUND::get);
        Comment comment = commentRepository.findById(commentId).orElseThrow(CommentException.NOT_FOUND::get);
        LikeComment likeComment = likeCommentRepository.findByMemberAndComment(member, comment).orElseThrow(LikeCommentException.NOT_FOUND::get);

        try {
            likeCommentRepository.delete(likeComment);
            comment.decrementLikeCount();
            log.info("__!_!_@!)@_~_!~__!~_!_!~~!_");
            log.info(comment.getLikeCount());
        } catch (Exception e) {
            throw LikeCommentException.NOT_DELETED.get();
        }
    }

    //좋아요 여부 확인
    public boolean isLikeComment(Long memberId, Long commentId) {
        Member member = memberRepository.findById(memberId).orElseThrow(MemberException.NOT_FOUND::get);
        Comment comment = commentRepository.findById(commentId).orElseThrow(CommentException.NOT_FOUND::get);

        return likeCommentRepository.existsByMemberAndComment(member, comment);
    }
}
