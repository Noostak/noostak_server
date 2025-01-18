package org.noostak.server.infra.error;

import org.noostak.server.global.error.core.BaseException;

public class S3Exception extends BaseException {
    public S3Exception(S3ErrorCode errorCode) {
        super(errorCode);
    }
}