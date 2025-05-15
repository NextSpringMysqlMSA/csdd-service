package com.nsmm.esg.csddservice.exception;

import org.springframework.http.HttpStatus;

/**
 * CSDD 리소스에 접근할 권한이 없는 사용자가 접근할 때 발생하는 예외 클래스
 * - 예: 로그인한 사용자가 본인이 등록하지 않은 데이터를 수정/삭제하려 할 때
 * - HTTP 상태 코드 403 (FORBIDDEN) 반환
 */
public class UnauthorizedCsddAccessException extends CsddException {

    /**
     * 권한 없는 접근 예외를 생성합니다.
     *
     * @param message 예외 메시지
     */
    public UnauthorizedCsddAccessException(String message) {
        super(message, HttpStatus.FORBIDDEN, "UNAUTHORIZED_ACCESS");
    }
}