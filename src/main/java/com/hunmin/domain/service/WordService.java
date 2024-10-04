package com.hunmin.domain.service;

import com.hunmin.domain.dto.page.WordPageRequestDTO;
import com.hunmin.domain.dto.word.WordRequestDTO;
import com.hunmin.domain.dto.word.WordResponseDTO;
import com.hunmin.domain.entity.Word;
import com.hunmin.domain.exception.WordCustomException;
import com.hunmin.domain.exception.WordException;
import com.hunmin.domain.repository.MemberRepository;
import com.hunmin.domain.repository.WordRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class WordService {
    private final WordRepository wordRepository;

    // 단어 등록
    public WordResponseDTO createWord(WordRequestDTO wordRequestDTO) {
        try {
            Word word = wordRequestDTO.toEntity();
            wordRepository.save(word);
            return new WordResponseDTO(word);
        } catch (Exception e) {
            throw new WordCustomException(WordException.WORD_NOT_REGISTERED);
        }
    }

    // 단어 조회
    public WordResponseDTO getWordByTitleAndLang(String title, String lang) {
        // title과 lang 조합으로 단어 찾기
        Word word = wordRepository.findByTitleAndLang(title, lang)
                .orElseThrow(() -> new WordCustomException(WordException.WORD_NOT_FOUND));
        return new WordResponseDTO(word);
    }

    // 전체 단어 조회
    public Page<WordResponseDTO> getAllWords(WordPageRequestDTO wordPageRequestDTO, String lang) {
        Pageable pageable = wordPageRequestDTO.getPageable(Sort.by("title")); // 기본 정렬 기준 설정
        Page<Word> words;

        // 언어가 null이거나 비어있을 경우 모든 언어의 단어 반환
        if (lang == null || lang.isEmpty()) {
            words = wordRepository.findAll(pageable);
        } else {
            words = wordRepository.findByLang(lang, pageable); // 선택된 언어의 단어 반환
        }

        // Page<Word>를 Page<WordResponseDTO>로 변환하여 반환
        return words.map(WordResponseDTO::new);
    }

    // 단어 수정
    public WordResponseDTO updateWord(WordRequestDTO wordRequestDTO, String title, String lang) {
        // title과 lang 조합으로 단어 조회
        Word word = wordRepository.findByTitleAndLang(title, lang)
                .orElseThrow(() -> new WordCustomException(WordException.WORD_NOT_FOUND));

        // 속성 수정
        word.changeWord(wordRequestDTO.getTitle()); // 새로운 제목으로 변경
        word.changeLang(wordRequestDTO.getLang());   // 새로운 언어로 변경
        word.changeTranslation(wordRequestDTO.getTranslation()); // 새로운 번역으로 변경
        word.changeDefinition(wordRequestDTO.getDefinition()); // 새로운 정의로 변경

        wordRepository.save(word); // 업데이트 반영
        return new WordResponseDTO(word);
    }

    // 단어 삭제
    public void deleteWordByTitleAndLang(String title, String lang) {
        // title과 lang 조합으로 단어 조회
        Word word = wordRepository.findByTitleAndLang(title, lang)
                .orElseThrow(() -> new WordCustomException(WordException.WORD_NOT_FOUND));

        // 단어 삭제
        wordRepository.delete(word);
    }
}
