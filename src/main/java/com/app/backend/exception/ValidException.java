package com.app.backend.exception;


import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ValidException extends RuntimeException{
    private final ErrorCode errorCode;
    private final Object data;

    public ValidException(ErrorCode errorCode, Object data) {
        super(errorCode.getDefaultMessage());
        this.errorCode = errorCode;
        this.data = data;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public Object getData() {
        return data;
    }
}
