package com.hunmin.domain.exception;

public enum NoticeException {
        NOTICE_NOT_FOUND("공지사항이 존재하지 않습니다.", 404),
        MEMBER_NOT_VALID("관리자 권한이 필요합니다.", 400),
        NOTICE_NOT_CREATED("공지사항 작성에 실패하였습니다.", 400),
        NOTICE_NOT_UPDATED("공지사항 수정에 실패하였습니다.", 400),
        NOTICE_NOT_DELETED("공지사항 삭제에 실패하였습니다.", 400);


    private NoticeTaskException noticeTaskException;

    NoticeException(String message, int code) {
        noticeTaskException = new NoticeTaskException(message, code);
    }

    public NoticeTaskException get(){
        return noticeTaskException;
    }
}
