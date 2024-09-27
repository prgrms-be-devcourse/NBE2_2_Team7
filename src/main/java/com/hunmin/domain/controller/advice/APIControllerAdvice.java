package com.hunmin.domain.controller.advice;

import com.hunmin.domain.exception.BoardTaskException;
import com.hunmin.domain.exception.CommentTaskException;
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
}
