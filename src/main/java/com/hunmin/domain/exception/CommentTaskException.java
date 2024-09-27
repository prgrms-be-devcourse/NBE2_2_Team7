package com.hunmin.domain.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CommentTaskException extends RuntimeException {
    private String message;
    private int code;
}
