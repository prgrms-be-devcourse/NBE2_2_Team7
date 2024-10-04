package com.hunmin.domain.repository;

import com.hunmin.domain.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    @Query("SELECT c FROM ChatRoom c JOIN Member m on c.member.memberId= :memberId")
    Optional<List<ChatRoom>> findByMemberId(@Param("memberId") Long memberId);
}
