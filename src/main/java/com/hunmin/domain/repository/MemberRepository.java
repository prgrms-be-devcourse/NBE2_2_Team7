package com.hunmin.domain.repository;

import com.hunmin.domain.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    
    // 회원 정보 조회
    Member findByEmail(String email);
    Optional<Member> findByNickname(String nickname);
    
    // 중복 체크
    boolean existsByEmail(String email);
    boolean existsByNickname(String nickname);
}
