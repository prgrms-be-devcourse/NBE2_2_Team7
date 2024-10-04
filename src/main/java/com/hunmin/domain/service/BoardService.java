package com.hunmin.domain.service;

import com.hunmin.domain.dto.board.BoardRequestDTO;
import com.hunmin.domain.dto.board.BoardResponseDTO;
import com.hunmin.domain.dto.page.PageRequestDTO;
import com.hunmin.domain.entity.Board;
import com.hunmin.domain.entity.Member;
import com.hunmin.domain.exception.BoardException;
import com.hunmin.domain.exception.MemberException;
import com.hunmin.domain.repository.BoardRepository;
import com.hunmin.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
@Log4j2
public class BoardService {
    private final MemberRepository memberRepository;
    private final BoardRepository boardRepository;

    //게시글 이미지 첨부
    public String uploadImage(MultipartFile file) throws IOException {
        String uploadDir = Paths.get("uploads").toAbsolutePath().normalize().toString();
        File directory = new File(uploadDir);

        if (!directory.exists()) {
            boolean created = directory.mkdirs();
            if (!created) {
                throw new IOException("Failed to create directory");
            }
        }

        String fileName = UUID.randomUUID() + "." + getFileExtension(file.getOriginalFilename());
        Path filePath = Paths.get(uploadDir, fileName);
        Files.copy(file.getInputStream(), filePath);

        return "/uploads/" + fileName;
    }

    //파일 확장자 추출
    private String getFileExtension(String fileName) {
        if (fileName == null || !fileName.contains(".")) {
            throw new IllegalArgumentException("Invalid file name: " + fileName);
        }

        return fileName.substring(fileName.lastIndexOf('.') + 1);
    }

    //게시글 이미지 삭제
    public void deleteImage(String imageUrl) throws IOException {
        String uploadDir = Paths.get("uploads").toAbsolutePath().normalize().toString();
        String fileName = imageUrl.substring(imageUrl.lastIndexOf("/") + 1);
        Path filePath = Paths.get(uploadDir, fileName);

        Files.deleteIfExists(filePath);
    }

    //게시글 등록
    public BoardResponseDTO createBoard(BoardRequestDTO boardRequestDTO) {
        try {
            Member member = memberRepository.findById(boardRequestDTO.getMemberId()).orElseThrow(MemberException.NOT_FOUND::get);

            Board board = Board.builder()
                    .member(member)
                    .nickname(member.getNickname())
                    .title(boardRequestDTO.getTitle())
                    .content(boardRequestDTO.getContent())
                    .location(boardRequestDTO.getLocation())
                    .latitude(boardRequestDTO.getLatitude())
                    .longitude(boardRequestDTO.getLongitude())
                    .imageUrls(boardRequestDTO.getImageUrls() != null ? boardRequestDTO.getImageUrls() : new ArrayList<>())
                    .build();

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
            List<String> existingImageUrls = new ArrayList<>(board.getImageUrls());

            if (boardRequestDTO.getImageUrls() != null) {
                List<String> newImageUrls = boardRequestDTO.getImageUrls();
                List<String> urlsToDelete = new ArrayList<>();

                for (String existingUrl : existingImageUrls) {
                    if (!newImageUrls.contains(existingUrl)) {
                        urlsToDelete.add(existingUrl);
                    }
                }

                for (String url : urlsToDelete) {
                    deleteImage(url);
                    existingImageUrls.remove(url);

                }

                existingImageUrls.addAll(newImageUrls);
                board.changeImgUrls(existingImageUrls);
            }

            board.changeTitle(boardRequestDTO.getTitle());
            board.changeContent(boardRequestDTO.getContent());
            board.changeLocation(boardRequestDTO.getLocation());
            board.changeLatitude(boardRequestDTO.getLatitude());
            board.changeLongitude(boardRequestDTO.getLongitude());

            boardRepository.save(board);

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
            for (String imageUrl : board.getImageUrls()) {
                deleteImage(imageUrl);
            }

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
