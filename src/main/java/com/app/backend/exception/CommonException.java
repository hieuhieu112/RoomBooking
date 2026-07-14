package com.app.backend.exception;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CommonException extends RuntimeException{
    private final ErrorCode errorCode;
    private final boolean autoHandleError;

    public CommonException(ErrorCode errorCode) {
        super(errorCode.getDefaultMessage());
        this.errorCode = errorCode;
        this.autoHandleError = true;
    }
    public CommonException(ErrorCode errorCode, boolean autoHandleError) {
        super(errorCode.getDefaultMessage());
        this.errorCode = errorCode;
        this.autoHandleError = autoHandleError;
    }
    public CommonException(ErrorCode errorCode, String message, boolean autoHandleError) {
        super(errorCode.getDefaultMessage());
        this.errorCode = errorCode;
        this.autoHandleError = autoHandleError;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
