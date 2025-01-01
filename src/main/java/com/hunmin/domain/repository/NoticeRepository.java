package com.hunmin.domain.repository;

import com.hunmin.domain.entity.Notice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoticeRepository extends JpaRepository<Notice, Long> {
    @Query("SELECT n FROM Notice n LEFT JOIN FETCH n.member ")
    Page<Notice> findAllNoticesResponse(Pageable pageable);
}
