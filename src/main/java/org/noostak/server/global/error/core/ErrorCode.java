package org.noostak.server.global.error.core;

import org.springframework.http.HttpStatus;

public interface ErrorCode {
    HttpStatus getStatus();
    String getMessage();
    int getStatusValue();
}
