package com.hunmin.domain.repository.search;

import com.hunmin.domain.dto.board.BoardResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BoardSearch {
    Page<BoardResponseDTO> searchBoard(Pageable pageable, String title);
}
