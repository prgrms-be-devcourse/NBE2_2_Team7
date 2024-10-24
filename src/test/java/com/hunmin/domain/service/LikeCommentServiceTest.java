package com.hunmin.domain.service;

import com.hunmin.domain.entity.Board;
import com.hunmin.domain.entity.Comment;
import com.hunmin.domain.entity.LikeComment;
import com.hunmin.domain.entity.Member;
import com.hunmin.domain.repository.CommentRepository;
import com.hunmin.domain.repository.LikeCommentRepository;
import com.hunmin.domain.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LikeCommentServiceTest {
    @InjectMocks
    private LikeCommentService likeCommentService;

    @Mock
    private LikeCommentRepository likeCommentRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private CommentRepository commentRepository;

    private Member member;
    private Board board;
    private Comment comment;
    private LikeComment likeComment;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        member = new Member(1L, "테스터");
        board = new Board(1L, member, "테스트 제목", "테스트 내용");
        comment = new Comment(1L, member, board, "테스트 댓글");
        likeComment = new LikeComment(1L, member, comment);
    }

    // 좋아요 등록 테스트
    @Test
    void createLikeComment() {
        when(memberRepository.findById(1L)).thenReturn(Optional.of(member));
        when(commentRepository.findById(1L)).thenReturn(Optional.of(comment));
        when(likeCommentRepository.findByMemberAndComment(any(Member.class), any(Comment.class))).thenReturn(Optional.empty());

        likeCommentService.createLikeComment(1L, 1L);

        verify(likeCommentRepository, times(1)).save(any(LikeComment.class));
    }

    //좋아요 삭제 테스트
    @Test
    void deleteLikeComment() {
        when(memberRepository.findById(1L)).thenReturn(Optional.of(member));
        when(commentRepository.findById(1L)).thenReturn(Optional.of(comment));
        when(likeCommentRepository.findByMemberAndComment(any(Member.class), any(Comment.class))).thenReturn(Optional.of(likeComment));

        likeCommentService.deleteLikeComment(1L, 1L);

        verify(likeCommentRepository, times(1)).delete(any(LikeComment.class));
    }

    //좋아요 여부 확인 테스트
    @Test
    void isLikeComment() {
        when(memberRepository.findById(1L)).thenReturn(Optional.of(member));
        when(commentRepository.findById(1L)).thenReturn(Optional.of(comment));
        when(likeCommentRepository.existsByMemberAndComment(any(Member.class), any(Comment.class))).thenReturn(true);

        boolean isLiked = likeCommentService.isLikeComment(1L, 1L);

        assertTrue(isLiked);
    }
}
