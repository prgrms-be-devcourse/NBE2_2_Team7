package com.hunmin.domain.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum WordException {

    WORD_NOT_FOUND("단어가 존재하지 않습니다.", 404),
    MEMBER_NOT_VALID("관리자 권한이 필요합니다.", 400),
    WORD_NOT_CREATED("단어 작성에 실패하였습니다.", 400),
    WORD_NOT_UPDATED("단어 수정에 실패하였습니다.", 400),
    WORD_NOT_DELETED("단어 삭제에 실패하였습니다.", 400);

    private WordTaskException wordTaskException;

    WordException(String message, int code){
        wordTaskException = new WordTaskException(message, code);
    }

    public WordTaskException get(){
        return wordTaskException;
    }
}