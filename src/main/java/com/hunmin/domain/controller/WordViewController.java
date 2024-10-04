package com.hunmin.domain.controller;

import com.hunmin.domain.dto.page.WordPageRequestDTO;
import com.hunmin.domain.dto.word.WordRequestDTO;
import com.hunmin.domain.dto.word.WordResponseDTO;
import com.hunmin.domain.entity.Word;
import com.hunmin.domain.exception.WordCustomException;
import com.hunmin.domain.service.WordViewService;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/words")
@RequiredArgsConstructor // 생성자 주입
public class WordViewController {

    private final WordViewService wordViewService; // 서비스 주입

    // 홈 페이지
    @GetMapping
    public String home(Model model) {
        return "word/wordhome"; // home.html 파일
    }

    // 단어 등록 화면 표시
    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        Long memberId = 2L; // 실제 로그인한 사용자 ID를 사용해야 함

        // 임시로 ADMIN 권한 부여
        if (memberId == 1L) { // 특정 ID에 대해 ADMIN으로 간주
            model.addAttribute("wordRequestDTO", new WordRequestDTO());
            return "word/wordRegister"; // wordRegister.html 파일
        }

        try {
            wordViewService.checkAdmin(memberId); // 관리자 권한 확인
        } catch (WordCustomException e) {
            model.addAttribute("errorMessage", "관리자 권한이 필요합니다."); // 에러 메시지 추가
            return "word/wordhome"; // 홈 페이지로 리다이렉트
//            return "redirect:/words"; // 홈 페이지로 리다이렉트
        }

        model.addAttribute("wordRequestDTO", new WordRequestDTO());
        return "word/wordRegister"; // wordRegister.html 파일
    }

    // 단어 등록 처리
    @PostMapping("/register")
    public String registerWord(@ModelAttribute WordRequestDTO wordRequestDTO) {
        wordRequestDTO.setMemberId(1L);
        wordViewService.createWord(wordRequestDTO); // 단어 등록 서비스 호출
        return "redirect:/words/list"; // 등록 후 목록으로 리다이렉트
    }

    // 단어 수정 입력 폼 표시
    @GetMapping("/update/input")
    public String showUpdateInputForm(Model model) {
        Long memberId = 2L; // 실제 로그인한 사용자 ID를 사용해야 함

        // 임시로 ADMIN 권한 부여
        if (memberId == 1L) { // 특정 ID에 대해 ADMIN으로 간주
            model.addAttribute("wordRequestDTO", new WordRequestDTO());
            return "word/WordUpdateInput"; // wordRegister.html 파일
        }

        try {
            wordViewService.checkAdmin(memberId); // 관리자 권한 확인
        } catch (WordCustomException e) {
            model.addAttribute("errorMessage", "관리자 권한이 필요합니다."); // 에러 메시지 추가
            return "word/wordhome"; // 홈 페이지로 리다이렉트
        }

        return "word/wordUpdateInput"; // 수정할 단어의 제목과 언어를 입력 받을 수 있는 페이지로 이동
    }

    // 단어 존재 여부 확인 및 수정 화면 표시
    @PostMapping("/update/check")
    public String checkUpdateWord(@RequestParam String title, @RequestParam String lang, Model model) {
        Long memberId = 2L; // 실제 로그인한 사용자 ID를 사용해야 함

        // 관리자 권한 확인
        if (!isAdmin(memberId)) {
            model.addAttribute("errorMessage", "관리자 권한이 필요합니다.");
            return "word/wordhome"; // 홈 페이지로 리다이렉트
        }

        // 입력받은 단어와 언어로 단어의 존재 여부를 확인
        boolean exists = wordViewService.existsByTitleAndLang(title, lang);

        // 해당 언어의 단어가 존재하지 않는 경우
        if (!exists) {
            model.addAttribute("errorMessage", "해당 언어의 단어가 존재하지 않습니다.");
            return "word/wordUpdateInput"; // 입력 페이지로 돌아감
        }

        // 해당 언어의 단어가 존재할 경우
        WordResponseDTO wordResponseDTO = wordViewService.getWordByTitleAndLang(title, lang);   // 단어 정보를 가져옴
        model.addAttribute("wordRequestDTO", wordResponseDTO);      // 가져온 단어 정보를 모델에 추가
        return "word/wordUpdate"; // 수정 화면으로 이동
    }

    // 단어 수정 처리
    @PostMapping("/update/submit")
    public String updateWord(@ModelAttribute WordRequestDTO wordRequestDTO) {
        wordRequestDTO.setMemberId(1L);
        // 수정된 단어 정보를 받아 업데이트
        wordViewService.updateWord(wordRequestDTO);
        return "redirect:/words/list"; // 수정 후 목록으로 리다이렉트
    }

    // 단어 조회
    @GetMapping("/view")
    public String viewWord(@RequestParam String title, @RequestParam String lang, Model model) {
        try {
            WordResponseDTO wordResponseDTO = wordViewService.getWordByTitleAndLang(title, lang);
            model.addAttribute("wordRequestDTO", wordResponseDTO);
            return "word/wordView"; // 단어 정보를 보여주는 페이지로 이동
        } catch (WordCustomException e) {
            model.addAttribute("errorMessage", "단어를 찾을 수 없습니다.");
            return "word/wordViewInput"; // 에러 발생 시 입력 페이지로 돌아감
        }
    }

    // 단어 조회 입력 폼
    @GetMapping("/view/input")
    public String viewInputForm(Model model) {
        // 언어 목록을 모델에 추가
        model.addAttribute("languages", List.of("모든 언어", "영어", "일본어", "중국어", "베트남어", "프랑스어"));
        return "word/wordViewInput"; // 단어 조회 입력 페이지로 이동
    }

    // 단어 리스트 조회
    @GetMapping("/list")
    public String listWords(Model model,
                            @RequestParam(value = "page", defaultValue = "1") int page,
                            @RequestParam(value = "size", defaultValue = "30") int size,
                            @RequestParam(value = "lang", required = false) String lang) {
        WordPageRequestDTO wordPageRequestDTO = WordPageRequestDTO.builder()
                .page(page)
                .size(size)
                .build();

        // lang이 비어있거나 null인 경우 모든 단어를 조회
        if (lang == null || lang.isEmpty()) {
            lang = ""; // 비어 있는 경우 전체 조회를 위해 빈 문자열로 설정
        }

        // 언어에 따라 단어를 조회
        Page<Word> wordPage = wordViewService.getAllWords(wordPageRequestDTO, lang);
        List<WordResponseDTO> words = wordPage.stream()
                .map(WordResponseDTO::new)
                .collect(Collectors.toList());

        model.addAttribute("words", words);
        model.addAttribute("page", page);
        model.addAttribute("totalPages", wordPage.getTotalPages());

        // 페이지 네비게이션 범위 계산
        int currentGroup = (page - 1) / 5; // 현재 페이지 그룹
        int startPage = currentGroup * 5 + 1; // 시작 페이지
        int endPage = Math.min(startPage + 4, wordPage.getTotalPages()); // 끝 페이지

        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("lang", lang); // 현재 선택된 언어를 모델에 추가

        return "word/wordList";
    }

    // 단어 삭제 확인 페이지로 이동
    @PostMapping("/delete/check")
    public String checkDeleteWord(@RequestParam String title, @RequestParam String lang, Model model) {
        Long memberId = 2L; // 실제 로그인한 사용자 ID를 사용해야 함

        // 관리자 권한 확인
        if (!isAdmin(memberId)) {
            model.addAttribute("errorMessage", "관리자 권한이 필요합니다.");
            return "word/wordhome"; // 홈 페이지로 리다이렉트
        }


        boolean exists = wordViewService.existsByTitleAndLang(title, lang);
        if (!exists) {
            model.addAttribute("errorMessage", "해당 언어의 단어가 존재하지 않습니다.");
            return "word/wordDeleteInput"; // 다시 입력 페이지로 이동
        }

        model.addAttribute("title", title);
        model.addAttribute("lang", lang); // 언어 추가
        return "word/wordDelete"; // 확인 페이지로 이동
    }

    // 단어 삭제 처리
    @PostMapping("/delete/{title}")
    public String deleteWord(@PathVariable String title, @RequestParam String lang) {
        Long memberId = 1L;
        System.out.println("Deleting word: " + title + " in language: " + lang);
        wordViewService.deleteWordByLang(title, lang, memberId); // 특정 언어의 단어 삭제 서비스 호출
        return "redirect:/words/list"; // 삭제 후 목록으로 리다이렉트
    }

    // 단어 삭제 입력 폼 표시
    @GetMapping("/delete/input")
    public String deleteInputForm(Model model) {
        Long memberId = 2L; // 실제 로그인한 사용자 ID를 사용해야 함

        // 임시로 ADMIN 권한 부여
        if (memberId == 1L) { // 특정 ID에 대해 ADMIN으로 간주
            model.addAttribute("wordRequestDTO", new WordRequestDTO());
            return "word/WordDeleteInput"; // wordRegister.html 파일
        }

        try {
            wordViewService.checkAdmin(memberId); // 관리자 권한 확인
        } catch (WordCustomException e) {
            model.addAttribute("errorMessage", "관리자 권한이 필요합니다."); // 에러 메시지 추가
            return "word/wordhome"; // 홈 페이지로 리다이렉트
        }
        return "word/wordDeleteInput"; // wordDeleteInput.html 파일
    }

    private boolean isAdmin(Long memberId) {
        return memberId == 1L; // 1L이 관리자의 ID라고 가정
    }
}

