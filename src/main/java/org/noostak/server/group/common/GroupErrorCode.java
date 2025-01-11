package org.noostak.server.group.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.noostak.server.global.error.core.ErrorCode;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum GroupErrorCode implements ErrorCode {
    INVALID_GROUP_NAME(HttpStatus.BAD_REQUEST, "유효하지 않은 그룹 이름입니다."),
    GROUP_NOT_FOUND(HttpStatus.NOT_FOUND, "그룹을 찾을 수 없습니다.");

    public static final String PREFIX = "[GROUP ERROR] ";

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