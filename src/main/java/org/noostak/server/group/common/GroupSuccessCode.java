package org.noostak.server.group.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.noostak.server.global.success.handler.SuccessCode;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum GroupSuccessCode implements SuccessCode {
    GROUP_CREATED(HttpStatus.CREATED, "그룹이 성공적으로 생성되었습니다."),
    GROUP_UPDATED(HttpStatus.OK, "그룹 정보가 성공적으로 수정되었습니다."),
    GROUP_DELETED(HttpStatus.OK, "그룹이 성공적으로 삭제되었습니다."),

    SUCCESS_GET_GROUP_LIST(HttpStatus.OK, "유저의 그룹 목록을 성공적으로 조회했습니다."),
    SUCCESS_CREATE_GROUP(HttpStatus.CREATED, "그룹 생성에 성공하였습니다."),
    ;

    private final HttpStatus status;
    private final String message;
}
