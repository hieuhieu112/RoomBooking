package com.app.backend.utils;

import com.app.backend.exception.ErrorCode;
import com.app.backend.exception.ValidException;
import com.app.backend.exception.utils.FieldErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class ValidRequestUtil {
    public static void validateRequest(BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {

            List<FieldErrorResponse> fieldErrors = bindingResult.getFieldErrors()
                    .stream()
                    .map(f -> new FieldErrorResponse(f.getField(), f.getDefaultMessage()))
                    .collect(Collectors.toList());

            throw new ValidException(
                    ErrorCode.VALIDATION,
                    fieldErrors
            );
        }
    }
}
