package org.noostak.server.infra.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.noostak.server.global.error.core.ErrorCode;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum S3ErrorCode implements ErrorCode {
    INVALID_EXTENSION(HttpStatus.BAD_REQUEST, "이미지 확장자는 jpg, png, webp만 가능합니다."),
    FILE_SIZE_EXCEEDED(HttpStatus.BAD_REQUEST, "이미지 사이즈는 2MB를 넘을 수 없습니다."),
    OBJECT_NOT_FOUND(HttpStatus.NOT_FOUND, "삭제하려는 이미지를 찾을 수 없습니다.");

    public static final String PREFIX = "[S3 ERROR] ";

    private final HttpStatus status;
    private final String rawMessage;

    @Override
    public String getMessage() {
        return PREFIX + rawMessage;
    }

    @Override
    public int getStatusValue() {
        return status.value();
    }
}