package com.hunmin.domain.repository;

import com.hunmin.domain.entity.Comment;
import com.hunmin.domain.entity.LikeComment;
import com.hunmin.domain.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeCommentRepository extends JpaRepository<LikeComment, Long> {
    //사용자와 댓글로 좋아요 조회
    Optional<LikeComment> findByMemberAndComment(Member member, Comment comment);

    //좋아요 여부 확인
    boolean existsByMemberAndComment(Member member, Comment comment);
}
