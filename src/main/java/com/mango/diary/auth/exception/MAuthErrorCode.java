package com.mango.diary.auth.exception;


import com.mango.diary.common.exception.ErrorCode;
import lombok.Getter;

@Getter
public enum MAuthErrorCode implements ErrorCode {
    UNAUTHORIZED(401, 1000, "인증되지 않은 사용자입니다."),
    EMAIL_ALREADY_EXISTS(409, 1001, "이미 사용중인 이메일입니다."),
    INVALID_INPUT(400, 1002, "입력값이 올바르지 않습니다."),
    EMAIL_NOT_FOUND(404, 1003, "존재하지 않는 회원 이메일입니다."),
    INVALID_VERIFICATION_CODE(400, 1004, "인증 코드가 일치하지 않습니다."),
    VERIFICATION_INCOMPLETE(400, 1005, "이메일 인증이 완료되지 않았습니다."),
    INCORRECT_INPUT(400, 1006, "회원정보가 올바르지 않습니다."),
    ALREADY_SIGN_OUT(401, 1007, "이미 로그아웃 된 사용자입니다."),
    INTERNAL_SERVER_ERROR(500, 1008, "서버 오류가 발생했습니다."),
    USER_NOT_FOUND(404, 1009, "사용자를 찾을 수 없습니다."),
    SAME_AS_PREVIOUS(400, 1010, "이전과 같은 닉네임 입니다."),
    //---------------------------------------------------------------------------------------
    INVALID_TOKEN_TYPE(400, 2000, "토큰의 타입이 유효하지 않습니다."),
    EXPIRED_TOKEN(401, 2001,"토큰이 만료되었습니다."),
    SECURITY_ERROR(401, 2003, "토큰 처리 중 보안 오류가 발생했습니다."),
    MALFORMED_TOKEN(401, 2004, "토큰 구조가 잘못되었습니다."),
    UNSUPPORTED_TOKEN(401, 2005,"토큰 유형이 지원되지 않습니다."),
    INVALID_TOKEN(401, 2006, "토큰이 유효하지 않거나 누락되었습니다."),
    INVALID_TOKEN_FORMAT(401, 2007,"토큰의 형식이 유효하지 않습니다."),
    JWT_ERROR(401, 2008, "토큰 관련 오류가 발생했습니다."),
    REFRESH_TOKEN_NOT_FOUND(404, 2009, "리프레시 토큰을 찾을 수 없습니다."),
    INVALID_REFRESH_TOKEN(400, 2010, "리프레시 토큰이 유효하지 않습니다."),
    KAKAO_UNAUTHORIZED(401, 4000, "카카오 인증 오류입니다."),
    KAKAO_FORBIDDEN(403, 4001, "카카오 접근이 금지되었습니다."),
    KAKAO_NOT_FOUND(404, 4002, "카카오 리소스를 찾을 수 없습니다."),
    KAKAO_BAD_REQUEST(400, 4003, "카카오 잘못된 요청입니다."),
    KAKAO_INTERNAL_SERVER_ERROR(500, 4004, "카카오 서버 오류가 발생했습니다."),
    ;


    private final int statusCode;
    private final int exceptionCode;
    private final String message;

    MAuthErrorCode(int statusCode, int exceptionCode, String message) {
        this.statusCode = statusCode;
        this.exceptionCode = exceptionCode;
        this.message = message;
    }
}
