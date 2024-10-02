package com.hunmin.domain.entity;

public enum MemberLevel {
    BEGINNER("초급"),
    INTERMEDIATE("중급"),
    ADVANCED("고급");

    private final String dispalyName;

    MemberLevel(String deispalyName) {
        this.dispalyName = deispalyName;
    }

    public String getDispalyName() {
        return dispalyName;
    }
}
