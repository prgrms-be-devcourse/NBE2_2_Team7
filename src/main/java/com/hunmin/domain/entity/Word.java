package com.hunmin.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Word {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long wordId;

    private String title;

    private String description;

    private String lang;
}