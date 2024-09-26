package com.hunmin.domain.service;

import com.hunmin.domain.dto.board.BoardRequestDTO;
import com.hunmin.domain.dto.board.BoardResponseDTO;
import com.hunmin.domain.dto.page.PageRequestDTO;
import com.hunmin.domain.entity.Board;
import com.hunmin.domain.entity.Member;
import com.hunmin.domain.exception.BoardException;
import com.hunmin.domain.repository.BoardRepository;
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
public class BoardService {
    private final MemberRepository memberRepository;
    private final BoardRepository boardRepository;

    //게시글 등록
    public BoardResponseDTO createBoard(BoardRequestDTO boardRequestDTO) {
        try {
            Member member = memberRepository.findById(boardRequestDTO.getMemberId()).orElseThrow();

            Board board = Board.builder().member(member).nickname(member.getNickname()).title(boardRequestDTO.getTitle()).content(boardRequestDTO.getContent())
                    .location(boardRequestDTO.getLocation()).latitude(boardRequestDTO.getLatitude()).longitude(boardRequestDTO.getLongitude()).build();

            boardRepository.save(board);

            return new BoardResponseDTO(board);
        } catch (Exception e) {
            log.error(e);
            throw BoardException.NOT_CREATED.get();
        }
    }

    //게시글 조회
    public BoardResponseDTO readBoard(Long boardId) {
        Board board = boardRepository.findById(boardId).orElseThrow(BoardException.NOT_FOUND::get);
        return new BoardResponseDTO(board);
    }

    //게시글 수정
    public BoardResponseDTO updateBoard(Long boardId, BoardRequestDTO boardRequestDTO) {
        Board board = boardRepository.findById(boardId).orElseThrow(BoardException.NOT_FOUND::get);

        try {
            board.changeTitle(boardRequestDTO.getTitle());
            board.changeContent(boardRequestDTO.getContent());

            return new BoardResponseDTO(board);
        } catch (Exception e) {
            log.error(e);
            throw BoardException.NOT_UPDATED.get();
        }
    }

    //게시글 삭제
    public BoardResponseDTO deleteBoard(Long boardId) {
        Board board = boardRepository.findById(boardId).orElseThrow(BoardException.NOT_FOUND::get);

        try {
            boardRepository.delete(board);

            return new BoardResponseDTO(board);
        } catch (Exception e) {
            log.error(e);
            throw BoardException.NOT_DELETED.get();
        }
    }

    //게시글 목록 조회
    public Page<BoardResponseDTO> readBoardList(PageRequestDTO pageRequestDTO) {
        Sort sort = Sort.by(Sort.Direction.DESC, "boardId");
        Pageable pageable = pageRequestDTO.getPageable(sort);

        Page<Board> boards = boardRepository.findAll(pageable);

        return boards.map(board -> new BoardResponseDTO(board));
    }

    //회원 별 작성글 목록 조회
    public Page<BoardResponseDTO> readBoardListByMember(Long memberId, PageRequestDTO pageRequestDTO) {
        Sort sort = Sort.by(Sort.Direction.DESC, "boardId");
        Pageable pageable = pageRequestDTO.getPageable(sort);

        Page<Board> boards = boardRepository.findByMemberId(memberId, pageable);

        return boards.map(board -> new BoardResponseDTO(board));
    }
}
