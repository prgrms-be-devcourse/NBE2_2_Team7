package com.hunmin.domain.dto.word;


import com.hunmin.domain.entity.Member;
import com.hunmin.domain.entity.Word;
import com.hunmin.domain.entity.WordScore;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class WordScoreRequestDTO {
    private Long wordScoreId;
    private Long memberId;
    private String testLang;
    private String testLevel;
    private int testScore;
    private Double testRankScore;

    private int correctCount;


    public WordScore toEntity(Member member) {
        return WordScore.builder()
                .wordScoreId(wordScoreId)
                .member(member)
                .testLang(testLang)
                .testLevel(testLevel)
                .testScore(this.testScore)
                .testRankScore(this.testRankScore)
                .build();
    }

    public void setScore(int finalScore, double penaltyScore) {
        this.testScore = finalScore;
        this.testRankScore = penaltyScore;
    }
}

