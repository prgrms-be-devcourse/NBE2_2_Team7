package com.hunmin.domain.entity;

import lombok.Getter;

@Getter
public enum MemberLevel {
    BEGINNER("초급"),
    INTERMEDIATE("중급"),
    ADVANCED("고급");

    private final String dispalyName;

    MemberLevel(String dispalyName) {
        this.dispalyName = dispalyName;
    }

}
