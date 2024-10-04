package com.hunmin.domain.entity;

<<<<<<< HEAD
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
=======
import jakarta.persistence.*;
>>>>>>> da13000e07de0fcea286b2011d11b9cd95be832d
import lombok.*;

@Entity
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Word extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long wordId;            // PK
    private String title;           // 단어(한국어)
    private String lang;            // 언어

    @Column(length = 1000)
    private String translation;     // 번역(영어)

    @Column(length = 1000)
    private String definition;      // 정의


    public void changeWord(String title){
        this.title = title;
    }

    public void changeTranslation(String translation){
        this.translation = translation;
    }

    public void changeLang(String lang){
        this.lang = lang;
    }

    public void changeDefinition(String definition){
        this.definition = definition;
    }
}