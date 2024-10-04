package com.hunmin.domain.service;

import com.hunmin.domain.dto.page.WordPageRequestDTO;
import com.hunmin.domain.dto.word.WordRequestDTO;
import com.hunmin.domain.dto.word.WordResponseDTO;
import com.hunmin.domain.entity.Member;
import com.hunmin.domain.entity.MemberRole;
import com.hunmin.domain.entity.Word;
import com.hunmin.domain.exception.WordException;
import com.hunmin.domain.exception.WordCustomException;
import com.hunmin.domain.repository.MemberRepository;
import com.hunmin.domain.repository.WordRepository;
import java.util.Optional;
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
public class WordViewService {
    private final WordRepository wordRepository;
    private final MemberRepository memberRepository;

    // 관리자 권한 확인 메소드
    public void checkAdmin(Long memberId) {
        Optional<Member> foundMember = memberRepository.findById(memberId);
        Member member = foundMember.orElseThrow(() -> new WordCustomException(WordException.MEMBER_NOT_FOUND));

        if (member.getMemberRole() != MemberRole.ADMIN) {
            throw new WordCustomException(WordException.MEMBER_NOT_ADMIN);
        }
    }

    // 단어 등록 - 관리자 권한
    public WordResponseDTO createWord(WordRequestDTO wordRequestDTO) {
        Optional<Member> foundMember = memberRepository.findById(wordRequestDTO.getMemberId());
        Member member = foundMember.orElseThrow(() -> new WordCustomException(WordException.MEMBER_NOT_FOUND));

        if (member.getMemberRole() == MemberRole.USER) {
            throw new WordCustomException(WordException.MEMBER_NOT_ADMIN);
        }

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
        Optional<Word> foundWord = wordRepository.findByTitleAndLang(title, lang);

        Word word = foundWord.orElseThrow(() -> new WordCustomException(WordException.WORD_NOT_FOUND));

        return new WordResponseDTO(word);
    }

    // 전체 단어 조회
    public Page<Word> getAllWords(WordPageRequestDTO wordPageRequestDTO, String lang) {
        Pageable pageable = wordPageRequestDTO.getPageable(Sort.by("title")); // 기본 정렬 기준 설정
        if (lang == null || lang.isEmpty()) {
            return wordRepository.findAll(pageable); // 모든 언어의 단어 반환
        } else {
            return wordRepository.findByLang(lang, pageable); // 선택된 언어의 단어 반환
        }
    }

    // 단어 수정 - 관리자 권한
    public WordResponseDTO updateWord(WordRequestDTO wordRequestDTO) {
        Word word = wordRepository.findById(wordRequestDTO.getWordId())
                .orElseThrow(() -> new WordCustomException(WordException.WORD_NOT_FOUND));

        Optional<Member> foundMember = memberRepository.findById(wordRequestDTO.getMemberId());
        Member member = foundMember.orElseThrow(() -> new WordCustomException(WordException.MEMBER_NOT_FOUND));

        if (member.getMemberRole() == MemberRole.USER) {
            throw new WordCustomException(WordException.MEMBER_NOT_ADMIN);
        }

        try {
            word.changeWord(wordRequestDTO.getTitle());
            word.changeTranslation(wordRequestDTO.getTranslation());
            word.changeDefinition(wordRequestDTO.getDefinition());
            word.changeLang(wordRequestDTO.getLang());

            wordRepository.save(word); // 추가 해야하는가..? word 객체의 업데이트가 반영되지 않아 호출을 필요로 한다고함

            return new WordResponseDTO(word);
        } catch (Exception e) {
            throw new WordCustomException(WordException.WORD_NOT_MODIFIED);
        }
    }

    // 단어 삭제 - 관리자 권한
    public void deleteWordByLang(String title, String lang, Long memberId) {
        // 관리자 권한 확인
        Optional<Member> foundMember = memberRepository.findById(memberId);
        Member member = foundMember.orElseThrow(() -> new WordCustomException(WordException.MEMBER_NOT_FOUND));

        if (member.getMemberRole() == MemberRole.USER) {
            throw new WordCustomException(WordException.MEMBER_NOT_ADMIN);
        }

        Word word = wordRepository.findByTitleAndLang(title, lang)
                .orElseThrow(() -> new WordCustomException(WordException.WORD_NOT_FOUND));

        try {
            wordRepository.delete(word);
        } catch (Exception e) {
            throw new WordCustomException(WordException.WORD_NOT_REMOVED);
        }
    }

    public boolean existsByTitleAndLang(String title, String lang) {
        return wordRepository.countByTitleAndLang(title, lang) > 0;
    }
}
