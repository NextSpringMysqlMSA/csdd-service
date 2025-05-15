package com.nsmm.esg.csddservice.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * CSDD 서비스에서 발생하는 커스텀 예외의 기본 클래스입니다.
 * 모든 사용자 정의 예외는 이 클래스를 상속받습니다.
 */
@Getter
public abstract class CsddException extends RuntimeException {

    private final HttpStatus status;
    private final String errorCode;

    protected CsddException(String message, HttpStatus status, String errorCode) {
        super(message);
        this.status = status;
        this.errorCode = errorCode;
    }
}