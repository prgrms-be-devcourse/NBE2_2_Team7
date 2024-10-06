package com.hunmin.domain.controller;

import com.hunmin.domain.dto.chat.ChatMessageDTO;
import com.hunmin.domain.dto.member.MemberDTO;
import com.hunmin.domain.service.ChatMessageService;
import com.hunmin.domain.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@Controller
@Log4j2
@RequestMapping("/api/chat")
@Tag(name = "채팅", description = "채팅 CRUD")
public class ChatMessageController {

    private final ChatMessageService chatMessageService;
    private final MemberService memberService;

    //클라이언트로 부터 오는 메세지 수신 -> Redis로 송신
    @MessageMapping("/api/chat/message")
    public void sendMessage(ChatMessageDTO message) {
        log.info("클라로부터온 메세지: {}", message.toString());
        chatMessageService.sendChatMessage(message);
    }
    //채팅 이력 조회
    @GetMapping("/messages/{chatRoomId}")
    @ResponseBody
    @Operation(summary = "과거 채팅 조회", description = "과거 채팅 내역을 조회하는 API")
    public List<ChatMessageDTO> readPastMessages(@PathVariable Long chatRoomId) {
        List<ChatMessageDTO> chatMessageDTOS = chatMessageService.readAllMessages(chatRoomId);
        log.info("->결과 chatMessageDTOS {}", chatMessageDTOS.toString());
        return chatMessageDTOS;
    }
    //채팅 조회
    @GetMapping("/{chatMessageId}")
    @ResponseBody
    @Operation(summary = "채팅 검색", description = "검색하고 싶은 채팅을 조회하는 API")
    public ResponseEntity<ChatMessageDTO> readMessage(@PathVariable Long chatMessageId) {
        return ResponseEntity.ok(chatMessageService.readChatMessage(chatMessageId));
    }
    //채팅 수정
    @PutMapping
    @ResponseBody
    @Operation(summary = "채팅 수정", description = "채팅 내역을 수정하는 API")
    public ResponseEntity<ChatMessageDTO> updateMessage(@RequestBody ChatMessageDTO message) {
        return ResponseEntity.ok(chatMessageService.updateChatMessage(message));
    }
    //채팅삭제
    @DeleteMapping("/{chatMessageId}")
    @ResponseBody
    @Operation(summary = "채팅 삭제", description = "삭제하고 싶은 채팅을 삭제하는 API")
    public ResponseEntity<Boolean> deleteMessage(@PathVariable Long chatMessageId) {
        return ResponseEntity.ok(chatMessageService.deleteChatMessage(chatMessageId));
    }
    @GetMapping("/user-info")
    @ResponseBody
    public MemberDTO getUserInfo(Authentication authentication) {
        String email = authentication.getName();
        return memberService.readUserInfo(email);
    }
}
