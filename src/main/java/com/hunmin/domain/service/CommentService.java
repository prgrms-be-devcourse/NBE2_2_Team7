package com.hunmin.domain.service;

import com.hunmin.domain.dto.comment.CommentRequestDTO;
import com.hunmin.domain.dto.comment.CommentResponseDTO;
import com.hunmin.domain.dto.page.PageRequestDTO;
import com.hunmin.domain.entity.Board;
import com.hunmin.domain.entity.Comment;
import com.hunmin.domain.entity.Member;
import com.hunmin.domain.exception.CommentException;
import com.hunmin.domain.repository.BoardRepository;
import com.hunmin.domain.repository.CommentRepository;
import com.hunmin.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Log4j2
public class CommentService {
    private final MemberRepository memberRepository;
    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;

    //댓글 등록
    public CommentResponseDTO createComment(Long boardId, CommentRequestDTO commentRequestDTO) {
        try {
            Member member = memberRepository.findById(commentRequestDTO.getMemberId()).orElseThrow();
            Board board = boardRepository.findById(boardId).orElseThrow();

            Comment comment = Comment.builder().member(member).board(board).content(commentRequestDTO.getContent()).build();

            commentRepository.save(comment);

            return new CommentResponseDTO(comment);
        } catch (Exception e) {
            log.error(e);
            throw CommentException.NOT_CREATED.get();
        }
    }

    //대댓글 등록
    public CommentResponseDTO createCommentChild(Long boardId, Long commentId, CommentRequestDTO commentRequestDTO) {
        try {
            Member member = memberRepository.findById(commentRequestDTO.getMemberId()).orElseThrow();
            Board board = boardRepository.findById(boardId).orElseThrow();
            Comment parent = commentRepository.findById(commentId).orElseThrow();

            Comment children = Comment.builder().member(member).board(board).parent(parent).content(commentRequestDTO.getContent()).build();

            commentRepository.save(children);

            return new CommentResponseDTO(children);
        } catch (Exception e) {
            log.error(e);
            throw CommentException.NOT_CREATED.get();
        }
    }

    //댓글 수정
    public CommentResponseDTO updateComment(Long commentId, CommentRequestDTO commentRequestDTO) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(CommentException.NOT_FOUND::get);

        try {
            comment.changeContent(commentRequestDTO.getContent());

            return new CommentResponseDTO(comment);
        } catch (Exception e) {
            log.error(e);
            throw CommentException.NOT_UPDATED.get();
        }
    }

    //댓글 삭제
    public CommentResponseDTO deleteComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(CommentException.NOT_FOUND::get);

        try {
            commentRepository.delete(comment);

            return new CommentResponseDTO(comment);
        } catch (Exception e) {
            log.error(e);
            throw CommentException.NOT_DELETED.get();
        }
    }

    //게시글 별 댓글 목록 조회
    public Page<CommentResponseDTO> readCommentList(Long boardId, PageRequestDTO pageRequestDTO) {
        Sort sort = Sort.by(Sort.Direction.ASC, "commentId");
        Pageable pageable = pageRequestDTO.getPageable(sort);
        Page<Comment> comments = commentRepository.findByBoardId(boardId, pageable);
        return comments.map(CommentResponseDTO::new);
    }
}
