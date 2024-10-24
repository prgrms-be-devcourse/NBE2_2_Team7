package com.hunmin.domain.repository;

import com.hunmin.domain.entity.Comment;
import com.hunmin.domain.entity.LikeComment;
import com.hunmin.domain.entity.Member;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class LikeCommentRepositoryTest {
    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private LikeCommentRepository likeCommentRepository;

    //사용자와 댓글로 좋아요 조회 테스트
    @Test
    void findByMemberAndComment() {
        Member member = memberRepository.findById(11L).get();
        Comment comment = commentRepository.findById(11L).get();

        LikeComment likeComment = LikeComment.builder()
                .member(member)
                .comment(comment)
                .build();
        likeCommentRepository.save(likeComment);

        Optional<LikeComment> foundLikeComment = likeCommentRepository.findByMemberAndComment(member, comment);

        assertTrue(foundLikeComment.isPresent());
        assertEquals(member.getMemberId(), foundLikeComment.get().getMember().getMemberId());
        assertEquals(comment.getCommentId(), foundLikeComment.get().getComment().getCommentId());
    }

    //좋아요 여부 확인 테스트
    @Test
    void existsByMemberAndComment() {
        Member member = memberRepository.findById(11L).get();
        Comment comment = commentRepository.findById(11L).get();

        LikeComment likeComment = LikeComment.builder()
                .member(member)
                .comment(comment)
                .build();
        likeCommentRepository.save(likeComment);

        boolean exists = likeCommentRepository.existsByMemberAndComment(member, comment);

        assertTrue(exists);
    }
}
