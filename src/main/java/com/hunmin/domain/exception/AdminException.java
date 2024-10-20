package com.hunmin.domain.exception;

public enum AdminException {
    MEMBERS_NOT_FOUND("회원 목록 조회에 실패 하였습니다.", 404),
    BOARDS_NOT_FOUND("회원의 게시글 목록 조회에 실패 하였습니다.", 404),
    COMMENTS_NOT_FOUND("회원의 댓글 목록 조회에 실패 하였습니다.", 404);

    private AdminTaskException adminTaskException;

    AdminException(String message, int code) {
        adminTaskException = new AdminTaskException(message, code);
    }

    public AdminTaskException get(){
        return adminTaskException;
    }
}
