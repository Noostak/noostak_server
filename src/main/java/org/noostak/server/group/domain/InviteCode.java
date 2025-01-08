package org.noostak.server.group.domain;

import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;

@Embeddable
@EqualsAndHashCode
public class InviteCode {

    private final String code;

    protected InviteCode() {
        this.code = null;
    }

    private InviteCode(String code) {
        validateInviteCode(code);
        this.code = code;
    }

    public static InviteCode from(String code) {
        return new InviteCode(code);
    }

    public String value() {
        return code;
    }

    private void validateInviteCode(String code) {
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
        if (code.length() != 6) {
            throw new IllegalArgumentException("[ERROR] 초대 코드는 정확히 6자리여야 합니다.");
        }
    }

    private void validateAlphaNumeric(String code) {
        if (!code.matches("^[a-zA-Z0-9]+$")) {
            throw new IllegalArgumentException("[ERROR] 초대 코드는 숫자와 알파벳으로만 구성되어야 합니다.");
        }
    }

    @Override
    public String toString() {
        return code;
    }
}
