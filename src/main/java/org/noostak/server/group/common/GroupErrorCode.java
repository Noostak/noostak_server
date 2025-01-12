package org.noostak.server.group.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.noostak.server.global.error.core.ErrorCode;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum GroupErrorCode implements ErrorCode {
    // 그룹 조회
    GROUP_NOT_FOUND(HttpStatus.NOT_FOUND, "그룹을 찾을 수 없습니다."),

    // 그룹 생성
    INVALID_GROUP_NAME(HttpStatus.BAD_REQUEST, "유효하지 않은 그룹 이름입니다."),
    INVALID_GROUP_NAME_LENGTH(HttpStatus.BAD_REQUEST, "그룹 이름의 길이는 30글자를 넘을 수 없습니다."),
    GROUP_NAME_NOT_EMPTY(HttpStatus.BAD_REQUEST, "그룹 이름은 비어 있을 수 없습니다."),
    INVALID_GROUP_IMAGE_URL(HttpStatus.BAD_REQUEST, "그룹 이미지 URL은 유효한 URL이어야 합니다."),
    INVALID_INVITE_CODE(HttpStatus.INTERNAL_SERVER_ERROR, "생성된 초대 코드가 정책에 부합하지 않습니다."),
    LEADER_NOT_FOUND(HttpStatus.NOT_FOUND, "리더를 찾을 수 없습니다."),
    LEADER_NOT_SET(HttpStatus.INTERNAL_SERVER_ERROR, "리더가 설정되지 않았습니다."),


    ;
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