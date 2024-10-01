package com.hunmin.domain.repository;

import com.hunmin.domain.entity.ChatMessage;
import com.hunmin.domain.repository.search.ChatMessageSearch;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long>, ChatMessageSearch {
}
