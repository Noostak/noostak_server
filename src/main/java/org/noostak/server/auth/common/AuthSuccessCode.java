package org.noostak.server.auth.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.noostak.server.global.success.handler.SuccessCode;

@Getter
@AllArgsConstructor
public enum AuthSuccessCode implements SuccessCode {
    VALID_JWT(200, "JWT 토큰이 유효합니다."),
    REFRESHED_JWT(200, "JWT 토큰이 성공적으로 갱신되었습니다."),
    LOGGED_IN(200, "로그인에 성공했습니다."),
    LOGGED_OUT(200, "로그아웃에 성공했습니다.");

    private final int status;
    private final String message;
}
