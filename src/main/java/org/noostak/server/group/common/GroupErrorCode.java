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
    LEADER_NOT_FOUND(HttpStatus.NOT_FOUND, "리더를 찾을 수 없습니다."),
    LEADER_NOT_SET(HttpStatus.INTERNAL_SERVER_ERROR, "리더가 설정되지 않았습니다."),

    // 초대 코드
    INVITE_CODE_NOT_EMPTY(HttpStatus.BAD_REQUEST, "초대 코드는 비어 있을 수 없습니다."),
    INVALID_INVITE_CODE(HttpStatus.INTERNAL_SERVER_ERROR, "생성된 초대 코드가 정책에 부합하지 않습니다."),
    INVALID_INVITE_CODE_LENGTH(HttpStatus.BAD_REQUEST, "초대 코드는 6자리여야 합니다."),
    INVALID_INVITE_CODE_ALPHA_NUMERIC_ONLY(HttpStatus.BAD_REQUEST, "초대 코드는 숫자와 알파벳으로만 구성되어야 합니다."),

    // 그룹 멤버 수와 관련된 에러 메시지
    MEMBER_COUNT_NEGATIVE(HttpStatus.BAD_REQUEST, "그룹 멤버 수는 음수가 될 수 없습니다."),
    MEMBER_COUNT_EXCEEDS_MAX_LIMIT(HttpStatus.BAD_REQUEST, "그룹 멤버 수는 최대 50명을 초과할 수 없습니다."),
    MEMBER_COUNT_INITIAL_NEGATIVE(HttpStatus.BAD_REQUEST, "초기 그룹 멤버 수는 음수가 될 수 없습니다.");
    ;


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