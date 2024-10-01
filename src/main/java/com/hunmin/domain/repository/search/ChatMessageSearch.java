package com.hunmin.domain.repository.search;

import com.hunmin.domain.dto.chat.ChatMessageListRequestDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ChatMessageSearch {
    Page<ChatMessageListRequestDTO> chatMessageList(Pageable pageable, Long chatRoomId);
}
