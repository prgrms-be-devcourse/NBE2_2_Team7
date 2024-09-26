package com.hunmin.repository;

import com.hunmin.domain.entity.Board;
import com.hunmin.domain.entity.Member;
import com.hunmin.domain.repository.BoardRepository;
import com.hunmin.domain.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class BoardRepositoryTests {
    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private BoardRepository boardRepository;

    //게시글 등록 테스트
    @Test
    @Transactional
    @Commit
    public void testCreateBoard() {
        Member member = memberRepository.findById(1L).get();

        Board board = Board.builder().member(member).title("게시글 등록 테스트").nickname(member.getNickname()).content("게시글 등록 테스트 내용입니다.").build();

        Board savedBoard = boardRepository.save(board);

        assertNotNull(savedBoard);
    }

    //게시글 조회 테스트
    @Test
    public void testBoardRead() {
        Long boardId = 1L;
        Board board = boardRepository.findById(boardId).orElseThrow();

        assertNotNull(board);
    }

    //게시글 수정 테스트
    @Test
    @Transactional
    @Commit
    public void testUpdateBoard() {
        Long boardId = 1L;
        String title = "게시글 수정 테스트";
        String content = "게시글 수정 테스트 내용";

        Board board = boardRepository.findById(boardId).orElseThrow();

        board.changeTitle(title);
        board.changeContent(content);

        board = boardRepository.findById(boardId).orElseThrow();

        assertEquals(title, board.getTitle());
        assertEquals(content, board.getContent());
    }

    //게시글 삭제 테스트
    @Test
    @Transactional
    @Commit
    public void testDeleteBoard() {
        Long boardId = 1L;

        boardRepository.deleteById(boardId);

        assertTrue(boardRepository.findById(boardId).isEmpty());
    }

    //게시글 목록 조회 테스트
    @Test
    public void testReadBoardList() {
        Pageable pageable = PageRequest.of(0, 20);

        Page<Board> boards = boardRepository.findAll(pageable);

        assertNotNull(boards);
    }
}
