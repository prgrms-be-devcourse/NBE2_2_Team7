package com.hunmin.domain.pubsub;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hunmin.domain.dto.chat.ChatMessageDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class RedisSubscriber implements MessageListener {

    private final ObjectMapper objectMapper;
    private final SimpMessageSendingOperations messagingTemplate;
    private final RedisTemplate redisTemplate;

    public void sendMessage(ChatMessageDTO publishMessage) {
        try {
            messagingTemplate.convertAndSend("/sub/chat/room/" + publishMessage.getChatRoomId(), publishMessage);
            log.info("[sendMessage]Sent message to STOMP topic: /sub/chat/room/{}", publishMessage.getChatRoomId());
        } catch (Exception e) {
            log.error("Exception {}", e);
        }
    }
    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            // redis 에서 발행된 데이터를 받아 역직렬화
            String publishMessage = (String) redisTemplate.getStringSerializer().deserialize(message.getBody());

            // 4. 해당 객체를 MessageDto 객체로 맵핑
            ChatMessageDTO chatMessage = objectMapper.readValue(publishMessage, ChatMessageDTO.class);

            // 5. Websocket 구독자에게 채팅 메시지 전송
            messagingTemplate.convertAndSend("/sub/chat/room/" + chatMessage.getChatRoomId(), chatMessage);
            log.info("[onMessage]Sent message to STOMP topic: /sub/chat/room/{}", chatMessage.getChatRoomId());
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}
