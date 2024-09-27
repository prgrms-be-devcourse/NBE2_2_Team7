package com.hunmin.domain.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.*;

@Entity
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Word extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long wordId;
    private String title;
    private String description;
    private String lang;

    public void changeWord(String title){
        this.title = title;
    }
    public void changeDescription(String description){
        this.description = description;
    }
    public void changeLang(String lang){
        this.lang = lang;
    }


}