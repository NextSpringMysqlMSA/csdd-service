package com.nsmm.esg.csddservice.exception;

import com.nsmm.esg.csddservice.dto.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * CSDD 서비스 전역 예외 처리 핸들러
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 커스텀 CSDD 예외 처리
     */
    @ExceptionHandler(CsddException.class)
    public ResponseEntity<ErrorResponse> handleCsddException(CsddException ex) {
        log.warn("[CSDD 예외] {}", ex.getMessage());
        ErrorResponse error = ErrorResponse.of(
                ex.getErrorCode(),
                ex.getMessage(),
                "" // 요청 경로를 추후 필요 시 추가 가능
        );
        return ResponseEntity.status(ex.getStatus()).body(error);
    }
}
