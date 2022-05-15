package com.example.backend.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    /* 404 NOT_FOUND : Resource 를 찾을 수 없음*/
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 유저를 찾을 수 없습니다."),
    BOOKMARK_LIST_NOT_FOUND(HttpStatus.NOT_FOUND,"북마크를 찾을 수 없습니다"),

    /* 409 CONFLICT : Resource 의 현재 상태와 충돌. 중복된 데이터 존재 */
    ALREADY_BOOKMARKED(HttpStatus.CONFLICT,"이미 북마크를 완료한 사용자입니다."),
    ALREADY_RESET_BOOKMARKED(HttpStatus.CONFLICT,"이미 북마크를 초기화한 사용자입니다."),
    ANALYSIS_NOT_WORKING(HttpStatus.CONFLICT, "분석이 작동하지 않습니다 다시 시도해주세요"),
    NEED_EMAIL(HttpStatus.CONFLICT, "이메일이 필요합니다");



    private final HttpStatus httpStatus;
    private final String detail;
}