package org.noostak.server.auth.common;

import org.noostak.server.global.error.core.BaseException;

public class AuthException extends BaseException {

    public AuthException(AuthErrorCode errorCode) {
        super(errorCode);
    }

    public AuthException(AuthErrorCode errorCode, Throwable cause) {
        super(errorCode);
        initCause(cause);
    }
}
