package com.hunmin.domain.controller.websocket;

import com.hunmin.domain.dto.chat.ChatMessageDTO;
import com.hunmin.domain.service.ChatMessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@Log4j2
public class ChatMessageController {

    private final ChatMessageService chatMessageService;

    @MessageMapping("/chat/message")
    public void sendMessage(ChatMessageDTO message) {
        log.info("Received message: {}", message.toString());
        chatMessageService.sendChatMessage(message); // Redis로 메시지 전송
    }
    @GetMapping("/chat/messages/{chatRoomId}")
    public List<ChatMessageDTO> readPastMessages(@PathVariable Long chatRoomId) {
        log.info("/messages/{chatRoomId {}", chatRoomId);
        List<ChatMessageDTO> chatMessageDTOS = chatMessageService.readAllMessages(chatRoomId);
        log.info("->결과 chatMessageDTOS {}", chatMessageDTOS.toString());
        return chatMessageDTOS;
    }
}
