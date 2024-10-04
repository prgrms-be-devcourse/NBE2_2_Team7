package com.hunmin.domain.controller;

import com.hunmin.domain.dto.page.WordPageRequestDTO;
import com.hunmin.domain.dto.word.WordRequestDTO;
import com.hunmin.domain.dto.word.WordResponseDTO;
import com.hunmin.domain.entity.Word;
import com.hunmin.domain.exception.WordCustomException;
import com.hunmin.domain.exception.WordException;
import com.hunmin.domain.repository.WordRepository;
import com.hunmin.domain.service.WordService;
<<<<<<< HEAD
=======
import com.hunmin.domain.service.WordViewService;
import java.util.Map;
>>>>>>> da13000e07de0fcea286b2011d11b9cd95be832d
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

    // 단어 등록
    @PostMapping
    public ResponseEntity<WordResponseDTO> createWord(@Validated @RequestBody WordRequestDTO wordRequestDTO) {
        return ResponseEntity.ok(wordService.createWord(wordRequestDTO));
    }

    // 단어 조회
    @GetMapping("/join/{title}/{lang}")
    public ResponseEntity<WordResponseDTO> read(@PathVariable("title") String title,
                                                @PathVariable("lang") String lang) {
        // 서비스 호출하여 단어 조회
        WordResponseDTO wordResponseDTO = wordService.getWordByTitleAndLang(title, lang);
        return ResponseEntity.ok(wordResponseDTO);
    }

    // 단어 전체 조회
    @GetMapping
    public ResponseEntity<Page<WordResponseDTO>> getWordlist(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "25") int size,
            @RequestParam(value = "lang", required = false) String lang) {

        // WordPageRequestDTO 객체 생성
        WordPageRequestDTO wordPageRequestDTO = WordPageRequestDTO.builder()
                .page(page)
                .size(size)
                .build();

        // Page<WordResponseDTO> 형태로 반환
        // 수정된 부분: 기존에 Page<Word>를 가져오고 다시 map을 사용하던 코드를 제거
        Page<WordResponseDTO> response = wordService.getAllWords(wordPageRequestDTO, lang);

        return ResponseEntity.ok(response); // 응답으로 반환
    }

    // 단어 수정
    @PutMapping("/update/{title}/{lang}")
    public ResponseEntity<WordResponseDTO> updateWord(@Validated @RequestBody WordRequestDTO wordRequestDTO,
                                                      @PathVariable("title") String title,
                                                      @PathVariable("lang") String lang) {
        // 서비스 호출하여 단어 수정
        WordResponseDTO updatedWord = wordService.updateWord(wordRequestDTO, title, lang);

        // 업데이트된 DTO 반환
        return ResponseEntity.ok(updatedWord);
    }

    // 단어 삭제
    @DeleteMapping("/delete/{title}/{lang}")
    public ResponseEntity<Map<String, String>> deleteWord(@PathVariable("title") String title, @PathVariable("lang") String lang){

        wordService.deleteWordByTitleAndLang(title, lang);

        return ResponseEntity.ok(Map.of("result", "success"));
    }
}
