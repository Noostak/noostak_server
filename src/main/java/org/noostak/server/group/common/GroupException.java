package org.noostak.server.group.common;

import org.noostak.server.global.error.core.BaseException;

public class GroupException extends BaseException {
    public GroupException(GroupErrorCode errorCode) {
        super(errorCode);
    }
}
