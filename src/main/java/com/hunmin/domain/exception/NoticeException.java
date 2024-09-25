package com.hunmin.domain.exception;

public enum NoticeException {
        NOT_FOUND_NOTICE("공지사항이 존재하지 않습니다.", 404),
        MEMBER_NOT_VALID("관리자 권한이 필요합니다.", 400);


    private NoticeTaskException noticeTaskException;

    NoticeException(String message, int code) {
        noticeTaskException = new NoticeTaskException(message, code);
    }

    public NoticeTaskException get(){
        return noticeTaskException;
    }
}
