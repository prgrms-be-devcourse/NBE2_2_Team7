package com.hunmin.domain.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum WordException {
    MEMBER_NOT_FOUND(NOT_FOUND, "회원 정보를 찾을 수 없습니다."),
    MEMBER_NOT_MATCHED(BAD_REQUEST, "회원 정보가 일치하지 않습니다."),
    MEMBER_NOT_REMOVED(CONFLICT, "회원 삭제에 실패했습니다."),
    MEMBER_NOT_REGISTERED(CONFLICT, "회원 등록에 실패했습니다."),
    MEMBER_NOT_MODIFIED(CONFLICT, "회원 정보 수정에 실패했습니다."),
    MEMBER_NOT_VALID(BAD_REQUEST, "유효하지 않은 관리자 코드입니다."),
    MEMBER_NOT_ADMIN(BAD_REQUEST, "관리자 권한이 필요합니다."),

    WORD_NOT_FOUND(NOT_FOUND, "단어 정보를 찾을 수 없습니다."),
    WORD_BAD_REQUEST(BAD_REQUEST, "단어 요청이 잘못되었습니다."),
    WORD_NOT_REMOVED(CONFLICT, "단어 삭제에 실패했습니다."),
    WORD_NOT_REGISTERED(CONFLICT, "단어 등록에 실패했습니다."),
    WORD_NOT_MODIFIED(CONFLICT, "단어 수정에 실패했습니다.");

    private final HttpStatus status;
    private final String message;
}
