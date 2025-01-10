package org.noostak.server.member.domain.vo;

import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;

@Embeddable
@EqualsAndHashCode
public class MemberName {
    private final String userName;

    protected MemberName() {
        this.userName = null;
    }

    public MemberName(String userName) {
        validateUserName(userName);
        this.userName = userName;
    }

    public String value() {
        return userName;
    }

    private void validateUserName(String userName) {
        validateEmpty(userName);
        validateLength(userName);
        validateSpecialLetter(userName);
    }

    private void validateEmpty(String userName) {
        if (userName == null || userName.isBlank()) {
            throw new IllegalArgumentException("[ERROR] 이름은 비어 있을 수 없습니다.");
        }
    }

    private void validateLength(String userName) {
        if (userName.length() > 15) {
            throw new IllegalArgumentException("[ERROR] 이름의 길이는 15글자를 넘을 수 없습니다.");
        }
    }

    private void validateSpecialLetter(String userName) {
        if (!userName.chars().allMatch(Character::isAlphabetic)) {
            throw new IllegalArgumentException("[ERROR] 이름은 알파벳 혹은 한글로만 구성이 가능합니다.");
        }
    }

    @Override
    public String toString() {
        return userName;
    }
}
