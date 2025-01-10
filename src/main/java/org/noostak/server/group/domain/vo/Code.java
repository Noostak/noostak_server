package org.noostak.server.group.domain.vo;

import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;

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
            throw new IllegalArgumentException("[ERROR] 초대 코드는 비어 있을 수 없습니다.");
        }
    }

    private void validateLength(String code) {
        if (code.length() != CODE_LENGTH) {
            throw new IllegalArgumentException("[ERROR] 초대 코드는 정확히 " + CODE_LENGTH + "자리여야 합니다.");
        }
    }

    private void validateAlphaNumeric(String code) {
        if (!code.matches("^[a-zA-Z0-9]+$")) {
            throw new IllegalArgumentException("[ERROR] 초대 코드는 숫자와 알파벳으로만 구성되어야 합니다.");
        }
    }

    @Override
    public String toString() {
        return value;
    }
}