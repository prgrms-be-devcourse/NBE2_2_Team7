package com.hunmin.domain.repository;

import com.hunmin.domain.entity.Board;
import com.hunmin.domain.entity.Comment;
import com.hunmin.domain.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    // 회원 정보 조회
    Member findByEmail(String email);

    // 중복 체크
    boolean existsByEmail(String email);
    // 이름으로 조회
    Optional<Member> findByNickname(String nickname);

    @Query("SELECT m FROM ChatRoom c join c.member m on c.chatRoomId= :chatRoomId")
    Optional<Member> findByChatRoomId(@Param("chatRoomId") Long chatRoomId);

    //모든 멤버 리스트로 조회
    Page<Member> findAll(Pageable pageable);

}
