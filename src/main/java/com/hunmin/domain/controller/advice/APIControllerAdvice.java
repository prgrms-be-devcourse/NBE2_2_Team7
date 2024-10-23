package com.hunmin.domain.controller.advice;

import com.hunmin.domain.exception.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class APIControllerAdvice {
    //게시글 예외 처리
    @ExceptionHandler(BoardTaskException.class)
    public ResponseEntity<Map<String, String>> handleBoardTaskException(BoardTaskException e) {
        Map<String, String> map = Map.of("error", e.getMessage());

        return ResponseEntity.status(e.getCode()).body(map);
    }

    //댓글 예외 처리
    @ExceptionHandler(CommentTaskException.class)
    public ResponseEntity<Map<String, String>> handleCommentTaskException(CommentTaskException e) {
        Map<String, String> map = Map.of("error", e.getMessage());

        return ResponseEntity.status(e.getCode()).body(map);
    }

    //공지 예외 처리
    @ExceptionHandler(NoticeTaskException.class)
    public ResponseEntity<Map<String, String>> handleNoticeTaskException(NoticeTaskException e) {
        Map<String, String> map = Map.of("error", e.getMessage());

        return ResponseEntity.status(e.getCode()).body(map);
    }

    //알림 예외 처리
    @ExceptionHandler(NotificationTaskException.class)
    public ResponseEntity<Map<String, String>> handleNotificationTaskException(NotificationTaskException e) {
        Map<String, String> map = Map.of("error", e.getMessage());

        return ResponseEntity.status(e.getCode()).body(map);
    }
    //채팅 예외 처리
    @ExceptionHandler(ChatMessageTaskException.class)
    public ResponseEntity<Map<String, String>> handleChatMessageTaskException(ChatMessageTaskException e) {
        Map<String, String> map = Map.of("error", e.getMessage());

        return ResponseEntity.status(e.getCode()).body(map);
    }
    //채팅룸 예외 처리
    @ExceptionHandler(ChatRoomTaskException.class)
    public ResponseEntity<Map<String, String>> handleChatRoomTaskException(ChatRoomTaskException e) {
        Map<String, String> map = Map.of("error", e.getMessage());

        return ResponseEntity.status(e.getCode()).body(map);
    }

    // 단어 예외 처리
    @ExceptionHandler(WordTaskException.class)
    public ResponseEntity<Map<String, String>> handleWordTaskException(WordTaskException e) {
        Map<String, String> map = Map.of("error", e.getMessage());

        return ResponseEntity.status(e.getCode()).body(map);
    }

    // 관리자 예외 처리
    @ExceptionHandler(AdminTaskException.class)
    public ResponseEntity<Map<String, String>> handleAdminTaskException(AdminTaskException e) {
        Map<String, String> map = Map.of("error", e.getMessage());

        return ResponseEntity.status(e.getCode()).body(map);
    }

    // 멤버 예외 처리
    @ExceptionHandler(MemberTaskException.class)
    public ResponseEntity<Map<String, String>> handleMemberTaskException(MemberTaskException e) {
        Map<String, String> map = Map.of("error", e.getMessage());

        return ResponseEntity.status(e.getCode()).body(map);
    }
}
