package com.hunmin.domain.repository;

import com.hunmin.domain.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    //게시글 별 댓글 목록 조회
    @Query("SELECT c FROM Comment c LEFT JOIN FETCH c.children WHERE c.board.boardId = :boardId AND c.parent IS NULL")
    Page<Comment> findByBoardId(@Param("boardId") Long boardId, Pageable pageable);
}
