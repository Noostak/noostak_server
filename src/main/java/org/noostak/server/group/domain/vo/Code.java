package org.noostak.server.group.domain.vo;

import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import org.noostak.server.group.common.GroupErrorCode;
import org.noostak.server.group.common.GroupException;

@Embeddable
@EqualsAndHashCode
public class Code {

    private static final int CODE_LENGTH = 6;

    private final String value;

    protected Code() {
        this.value = null;
    }

    private Code(String code) {
        validate(code);
        this.value = code;
    }

    public static Code from(String code) {
        return new Code(code);
    }

    public String value() {
        return value;
    }

    private void validate(String code) {
        validateNotEmpty(code);
        validateLength(code);
        validateAlphaNumeric(code);
    }

    private void validateNotEmpty(String code) {
        if (code == null || code.isBlank()) {
            throw new GroupException(GroupErrorCode.INVITE_CODE_NOT_EMPTY);
        }
    }

    private void validateLength(String code) {
        if (code.length() != CODE_LENGTH) {
            throw new GroupException(GroupErrorCode.INVALID_INVITE_CODE_LENGTH);
        }
    }

    private void validateAlphaNumeric(String code) {
        if (!code.matches("^[a-zA-Z0-9]+$")) {
            throw new GroupException(GroupErrorCode.INVALID_INVITE_CODE_ALPHA_NUMERIC_ONLY);
        }
    }

    @Override
    public String toString() {
        return value;
    }
}