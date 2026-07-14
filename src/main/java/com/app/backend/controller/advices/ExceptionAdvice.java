package com.app.backend.controller.advices;

import com.app.backend.dtos.response.DataResponse;
import com.app.backend.exception.CommonException;
import com.app.backend.exception.ErrorCode;
import com.app.backend.exception.ValidException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ExceptionAdvice {

    @ExceptionHandler(CommonException.class)
    public ResponseEntity<DataResponse<?>> handleCommonException(CommonException ex, HttpServletRequest req) {

        ErrorCode ec = ex.getErrorCode();

        DataResponse<?> response = DataResponse.builder()
                .statusCode(ec.getCode())
                .message(ec.getDefaultMessage())
                .data(ec.getDefaultMessage())
                .build();

        return ResponseEntity.status(ec.getHttpStatus()).body(response);
    }






    @ExceptionHandler(ValidException.class)
    public ResponseEntity<DataResponse<?>> handleValidationException(ValidException ex, HttpServletRequest req) {
        ErrorCode ec = ex.getErrorCode();

        DataResponse<?> response = DataResponse.builder()
                .statusCode(ec.getCode())          // ERROR.VALIDATION
                .message(ec.getDefaultMessage())          // "Validation failed"
                .data(ex.getData())                // List<FieldErrorResponse>
                .build();

        return ResponseEntity.status(ec.getHttpStatus()).body(response); // 400
    }
    // nay la fall back
    @ExceptionHandler(Exception.class)
    public ResponseEntity<DataResponse<?>> handleUnknown(
            Exception ex,
            HttpServletRequest req
    ) {
        log.error("Exception at {} {}: ", req.getMethod(), req.getRequestURI(), ex);

        DataResponse<?> response = DataResponse.builder()
                .statusCode("INTERNAL_ERROR")
                .message("Lỗi hệ thống")
                .data(null)
                .build();
        return ResponseEntity.internalServerError().body(response);
    }
}
