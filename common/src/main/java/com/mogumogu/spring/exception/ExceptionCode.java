package com.mogumogu.spring.exception;


import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
@AllArgsConstructor
public enum ExceptionCode {

    /**
     * 200 SUCCESSFUL : 정상처리
     */
    SUCCESSFULL(OK, "정상처리 되었습니다"),
    DELETE_SUCCESSFUL(OK, "정상적으로 삭제되었습니다."),


    /**
     * 400 BAD_REQUEST : 잘못된 요청
     */
    INVALID_REFRESH_TOKEN(BAD_REQUEST, "리프레시 토큰이 유효하지 않습니다."),
    MISMATCH_REFRESH_TOKEN(BAD_REQUEST, "리프레시 토큰의 유저 정보가 일치하지 않습니다."),
    CREATE_FAIL_USER(BAD_REQUEST, "회원 가입에 실패하였습니다."),
    DEPOSIT_BUTTON_FAIL(BAD_REQUEST, "입금 버튼 푸쉬 실패하였습니다."),
    RECRUIT_ING(BAD_REQUEST, "모집이 중 입니다!."),

    /**
     * 401 UNAUTHORIZED : 인증되지 않은 사용자
     */
    INVALID_AUTH_TOKEN(UNAUTHORIZED, "권한 정보가 없는 토큰입니다."),
    UNAUTHORIZED_USER(UNAUTHORIZED, "존재하지 않는 계정 정보입니다"),
    UNAUTHRORIZED_ADMIN(UNAUTHORIZED, "존재하지 않는 관리자 정보입니다."),



    /**
     * 404 NOT_FOUND : 해당 Resources 찾을 수 없음
     */
    USER_NOT_FOUND(NOT_FOUND, "해당 유저 정보를 찾을 수 없습니다"),
    ADMIN_NOT_FOUND(NOT_FOUND, "해당 관리자 정보를 찾을 수 업습니다"),
    REFRESH_TOKEN_NOT_FOUND(NOT_FOUND, "로그아웃 된 사용자입니다"),
    ARTICLE_NOT_EXIST(NOT_FOUND, "해당 게시물이 존재하지 않습니다"),
    Message_IS_NOT_EXIST(NOT_FOUND, "해당 쪽지가 존재하지 않습니다"),
    KEYWORD_IS_NOT_EXIST(NOT_FOUND, "해당 키워드가 존재하지 않습니다"),
    UNABLE_TO_SEND_EMAIL(NOT_FOUND, "메일을 보낼 수 없습니다"),
    NO_SUCH_ALGORITHM(NOT_FOUND, "인증 코드를 생성할 수 없습니다"),
    EMAIL_AUTH_NOT_FOUND(NOT_FOUND, "인증 코드를 확인할 수 없습니다."),


    /**
     * 409 CONFLICT : 현재 서버와의 충돌 (보통 업로드시)
     */
    UPDATE_FAIL_ADMIN(CONFLICT, "관리자 정보수정에 실패하였습니다"),
    UPDATE_FAIL_USER(CONFLICT, "유저 정보수정에 실패하였습니다"),
    DUPLICATE_NICKNAME(CONFLICT, "해당 닉네임은 이미 존재합니다"),
    LOGIN_IS_WRONG(CONFLICT, "아이디 또는 비밀번호가 잘못 입력되었습니다."),

    ;


    private final HttpStatus httpStatus;
    private final String message;


}

