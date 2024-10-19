package com.hunmin.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

// refresh 토큰 저장을 위한 엔티티
@Entity
@Getter
@Setter
@Table(name = "refresh_tokens")
public class RefreshEntity {

    @Id
    @Column(nullable = false, unique = true)
    private String email;

    private String refresh;
    private String expiration;

}
