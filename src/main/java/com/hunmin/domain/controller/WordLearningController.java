package com.hunmin.domain.controller;

import com.hunmin.domain.dto.word.WordResponseDTO;
import com.hunmin.domain.service.WordLearningService;
import java.util.Collections;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RequestMapping("/words/learning")
public class WordLearningController {
    private final WordLearningService wordLearningService;

    @Autowired
    public WordLearningController(WordLearningService wordLearningService) {
        this.wordLearningService = wordLearningService;
    }

    @GetMapping("/languageSelect")
    public String getLearningLanguagePage() {
        return "word/learningLanguage"; // word/learningLanguage.html 파일을 렌더링합니다.
    }

    @GetMapping("/levelSelect")
    public String levelPage(@RequestParam String lang, Model model) {
        model.addAttribute("lang", lang); // 선택한 언어를 모델에 추가
        return "word/levelSelect"; // 레벨 선택 페이지로 이동
    }

    @GetMapping("/start")
    public String getRandomWords(@RequestParam String lang, @RequestParam int level, Model model) {
        // 선택한 언어와 레벨에 따른 랜덤 단어 가져오기
        List<WordResponseDTO> randomWords = wordLearningService.getRandomWords(lang, level);

        // 모든 단어의 displayTime을 모델에 추가
        model.addAttribute("words", randomWords); // 가져온 단어들을 모델에 추가
        model.addAttribute("level", level); // 선택한 레벨을 모델에 추가
        model.addAttribute("lang", lang); // 선택한 언어를 모델에 추가

        // 단어의 displayTime을 별도로 전달
        long displayTime = randomWords.isEmpty() ? 0 : randomWords.get(0).getDisplayTime();
        model.addAttribute("displayTime", displayTime); // displayTime 추가

        return "word/learningWord"; // 단어 표시 페이지로 이동
    }
}
