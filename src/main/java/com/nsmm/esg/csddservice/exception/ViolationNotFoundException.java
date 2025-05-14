package com.nsmm.esg.csddservice.exception;

import org.springframework.http.HttpStatus;

/**
 * 존재하지 않는 위반 항목 ID가 조회 요청에 포함된 경우 발생하는 예외입니다.
 */
public class ViolationNotFoundException extends CsddException {
    public ViolationNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND, "VIOLATION_NOT_FOUND");
    }
}