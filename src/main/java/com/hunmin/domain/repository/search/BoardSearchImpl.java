package com.hunmin.domain.repository.search;

import com.hunmin.domain.dto.board.BoardResponseDTO;
import com.hunmin.domain.entity.Board;
import com.hunmin.domain.entity.QBoard;
import com.hunmin.domain.entity.QComment;
import com.querydsl.jpa.JPQLQuery;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;
import java.util.stream.Collectors;

@Log4j2
public class BoardSearchImpl extends QuerydslRepositorySupport implements BoardSearch {

    public BoardSearchImpl() {
        super(Board.class);
    }

    @Override
    public Page<BoardResponseDTO> searchBoard(Pageable pageable, String title) {
        QBoard board = QBoard.board;
        QComment comment = QComment.comment;

        JPQLQuery<Board> query = from(board)
                .leftJoin(board.member).fetchJoin()
                .leftJoin(board.comments, comment).fetchJoin()
                .where(board.title.contains(title))
                .distinct();

        long total = query.fetchCount();

        getQuerydsl().applyPagination(pageable, query);

        List<Board> boardList = query.fetch();

        List<BoardResponseDTO> boardDtoList = boardList.stream()
                .map(BoardResponseDTO::new)
                .collect(Collectors.toList());

        return new PageImpl<>(boardDtoList, pageable, total);
    }
}
