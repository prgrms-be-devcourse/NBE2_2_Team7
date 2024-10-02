package com.hunmin.domain.repository;

import com.hunmin.domain.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    // 회원 정보 조회
    Member findByEmail(String email);

    // 중복 체크
    boolean existsByEmail(String email);
    // 이름으로 조회
    Optional<Member> findByNickname(String nickname);
