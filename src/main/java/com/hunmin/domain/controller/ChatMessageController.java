package com.hunmin.domain.controller;

import com.hunmin.domain.dto.chat.ChatMessageDTO;
import com.hunmin.domain.service.ChatMessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@RequiredArgsConstructor
@Controller
@Log4j2
@RequestMapping("/api/chat")
public class ChatMessageController {

    private final ChatMessageService chatMessageService;

    //클라이언트로 부터 오는 메세지 수신 -> Redis로 송신
    @MessageMapping("/api/chat/message")
    public void sendMessage(ChatMessageDTO message) {
        log.info("클라로부터온 메세지: {}", message.toString());
        chatMessageService.sendChatMessage(message);
    }
    //채팅 이력 조회
    @GetMapping("/messages/{chatRoomId}")
    @ResponseBody
    public List<ChatMessageDTO> readPastMessages(@PathVariable Long chatRoomId) {
        log.info("/messages/{chatRoomId {}", chatRoomId);
        List<ChatMessageDTO> chatMessageDTOS = chatMessageService.readAllMessages(chatRoomId);
        log.info("->결과 chatMessageDTOS {}", chatMessageDTOS.toString());
        return chatMessageDTOS;
    }
//     아직 미완성 페이징 기술
//    @GetMapping("/messages/{chatRoomId}")
//    @ResponseBody
//    public ResponseEntity<Page<ChatMessageListRequestDTO>> loadMessageList(@PathVariable Long chatRoomId,
//                                                                           @RequestParam(value = "page", defaultValue = "1") int page,
//                                                                           @RequestParam(value = "size", defaultValue = "10") int size) {
//        ChatMessagePageRequestDTO chatMessagePageRequestDTO = ChatMessagePageRequestDTO.builder().page(page).size(size).build();
//        log.info("/loadMessageList {}", chatMessagePageRequestDTO.toString());
//        return ResponseEntity.ok(chatMessageService.getList(chatMessagePageRequestDTO, chatRoomId));
//    }
}
