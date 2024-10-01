package com.hunmin.domain.controller.advice;

import com.hunmin.domain.exception.BoardTaskException;
import com.hunmin.domain.exception.CommentTaskException;
import com.hunmin.domain.exception.NotificationTaskException;
import com.hunmin.domain.exception.NoticeTaskException;
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
}
