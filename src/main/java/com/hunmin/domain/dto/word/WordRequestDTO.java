package com.hunmin.domain.dto.word;

import com.hunmin.domain.entity.Word;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class WordRequestDTO {

    @NotNull
    private Long wordId;

    @NotNull
    private Long memberId;

    @NotEmpty(message = "명칭은 필수 입력값입니다.")
    private String title;

    @NotEmpty(message = "내용은 필수 입력값입니다.")
    private String description;

    @NotEmpty(message = "언어는 필수 입력값입니다.")
    private String lang;

    public Word toEntity() {
        return Word.builder()
                .wordId(wordId)
                .title(title)
                .description(description)
                .lang(lang)
                .build();
    }
}