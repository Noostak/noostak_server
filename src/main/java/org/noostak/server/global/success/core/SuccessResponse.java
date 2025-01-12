package org.noostak.server.global.success.core;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.noostak.server.global.success.handler.SuccessCode;
import org.springframework.http.HttpStatus;

public record SuccessResponse<T>(
        HttpStatus status,
        String message,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        T result
) {
    public static <T> SuccessResponse<T> of(SuccessCode successCode) {
        return new SuccessResponse<>(successCode.getStatus(), successCode.getMessage(), null);
    }

    public static <T> SuccessResponse<T> of(SuccessCode successCode, T result) {
        return new SuccessResponse<>(successCode.getStatus(), successCode.getMessage(), result);
    }
}