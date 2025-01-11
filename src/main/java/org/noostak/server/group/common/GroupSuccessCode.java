package org.noostak.server.group.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.noostak.server.global.success.handler.SuccessCode;

@Getter
@AllArgsConstructor
public enum GroupSuccessCode implements SuccessCode {
    GROUP_CREATED(201, "그룹이 성공적으로 생성되었습니다."),
    GROUP_UPDATED(200, "그룹 정보가 성공적으로 수정되었습니다."),
    GROUP_DELETED(200, "그룹이 성공적으로 삭제되었습니다.");

    private final int status;
    private final String message;
}
