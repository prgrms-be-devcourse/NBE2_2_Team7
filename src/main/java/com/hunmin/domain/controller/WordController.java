package com.hunmin.domain.controller;

import com.hunmin.domain.dto.page.WordPageRequestDTO;
import com.hunmin.domain.dto.word.WordRequestDTO;
import com.hunmin.domain.dto.word.WordResponseDTO;
import com.hunmin.domain.entity.Word;
import com.hunmin.domain.exception.WordCustomException;
import com.hunmin.domain.exception.WordException;
import com.hunmin.domain.repository.WordRepository;
import com.hunmin.domain.service.WordService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/words")
public class WordController {
    private final WordService wordService;
    private final WordRepository wordRepository;

    // 단어 등록 - 관리자 권한
    @PostMapping
    public ResponseEntity<WordResponseDTO> createWord(@Validated @RequestBody WordRequestDTO wordRequestDTO) {
        return ResponseEntity.ok(wordService.createWord(wordRequestDTO));
    }

    // 단어 조회
    @GetMapping("/title/{title}")
    public ResponseEntity<WordResponseDTO> read(@PathVariable("title") String title) {
        return ResponseEntity.ok(wordService.getWordByTitle(title));
    }

    // 단어 전체 조회 1
    @GetMapping
    public ResponseEntity<Page<Word>> getWordlist(@RequestParam(value = "page", defaultValue = "1") int page,
                                                  @RequestParam(value = "size", defaultValue = "20") int size) {
        WordPageRequestDTO wordPageRequestDTO = WordPageRequestDTO.builder().page(page).size(size).build();

        return ResponseEntity.ok(wordService.getAllWords(wordPageRequestDTO));
    }

//    // 단어 전체 조회 2
//    @GetMapping
//    public ResponseEntity<Page<Word>> getWordlist(@Validated WordPageRequestDTO wordPageRequestDTO) {
//        return ResponseEntity.ok(wordService.getAllWords(wordPageRequestDTO));
//    }

    // 단어 수정 - 관리자 권한 , 요청 시 BODY 에 wordId와 DB wordId 일치해야함
    @PutMapping("/title/{title}")
    public ResponseEntity<WordResponseDTO> updateWord(@Validated @RequestBody WordRequestDTO wordRequestDTO,
                                                      @PathVariable("title") String title) {

        Word word = wordRepository.findById(wordRequestDTO.getWordId()).orElseThrow(() -> new WordCustomException(
                WordException.WORD_NOT_FOUND));

        if (!word.getTitle().equals(title)) {
            throw new WordCustomException(WordException.WORD_BAD_REQUEST);
        }

        return ResponseEntity.ok(wordService.updateWord(wordRequestDTO));
    }

    // 단어 삭제 - 관리자 권한
    @DeleteMapping("/title/{title}")
    public ResponseEntity<Map<String, String>> deleteWord(@PathVariable("title") String title) {
        wordService.deleteWord(title);

        return ResponseEntity.ok(Map.of("result", "success"));
    }
}
