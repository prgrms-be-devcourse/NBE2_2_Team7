package com.hunmin.domain.dto.word;

import com.hunmin.domain.entity.Word;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class WordResponseDTO {
    private Long wordId;                // PK
    private String title;               // 명칭
    private String description;         // 내용
    private String lang;                // 언어
    private LocalDateTime createdAt;    // 작성날짜
    private LocalDateTime updatedAt;    // 수정날짜

    public WordResponseDTO(Word word) {
        this.wordId = word.getWordId();
        this.title = word.getTitle();
        this.description = word.getDescription();
        this.lang = word.getLang();
        this.createdAt = word.getCreatedAt();
        this.updatedAt = word.getUpdatedAt();
    }
}
