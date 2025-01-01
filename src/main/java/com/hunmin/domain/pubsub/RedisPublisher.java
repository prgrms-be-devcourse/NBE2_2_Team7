package com.hunmin.domain.pubsub;

import com.hunmin.domain.dto.chat.ChatMessageDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Log4j2
@Component
@RequiredArgsConstructor
public class RedisPublisher {

    private final RedisTemplate<String, Object> redisTemplate;
    //레디스함 채널명
    private static final String CHAT_CHANNEL = "chat_channel";

    //레디스함으로 메세지 송신
    public void publish(ChatMessageDTO message) {
        redisTemplate.convertAndSend(CHAT_CHANNEL, message);
    }
}
