package com.nsmm.esg.csddservice.exception;

import org.springframework.http.HttpStatus;

/**
 * 설문 응답 요청이 유효하지 않거나 누락된 필드가 있을 때 발생하는 예외입니다.
 */
public class InvalidAnswerException extends CsddException {
    public InvalidAnswerException(String message) {
        super(message, HttpStatus.BAD_REQUEST, "INVALID_ANSWER");
    }
}