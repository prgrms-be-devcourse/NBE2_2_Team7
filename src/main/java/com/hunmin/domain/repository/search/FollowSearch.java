package com.hunmin.domain.repository.search;

import com.hunmin.domain.dto.follow.FollowRequestDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FollowSearch {
    Page<FollowRequestDTO> getFollowPage(Long memberId, Pageable pageable);
}
