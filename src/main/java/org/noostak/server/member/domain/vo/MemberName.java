package org.noostak.server.member.domain.vo;

import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;

@Embeddable
@EqualsAndHashCode
public class MemberName {
    private final String memberName;

    protected MemberName() {
        this.memberName = null;
    }

    private MemberName(String memberName) {
        validateMemberName(memberName);
        this.memberName = memberName;
    }

    public static MemberName from(String memberName) {
        return new MemberName(memberName);
    }

    public String value() {
        return memberName;
    }

    private void validateMemberName(String memberName) {
        validateEmpty(memberName);
        validateLength(memberName);
        validateSpecialLetter(memberName);
    }

    private void validateEmpty(String memberName) {
        if (memberName == null || memberName.isBlank()) {
            throw new IllegalArgumentException("[ERROR] 멤버 이름은 비어 있을 수 없습니다.");
        }
    }

    private void validateLength(String memberName) {
        if (memberName.length() > 15) {
            throw new IllegalArgumentException("[ERROR] 멤버 이름의 길이는 15글자를 넘을 수 없습니다.");
        }
    }

    private void validateSpecialLetter(String memberName) {
        if (!memberName.chars().allMatch(Character::isAlphabetic)) {
            throw new IllegalArgumentException("[ERROR] 멤버 이름은 알파벳 혹은 한글로만 구성이 가능합니다.");
        }
    }

    @Override
    public String toString() {
        return memberName;
    }
}
