package org.noostak.server.group.domain.vo;

import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;

@Embeddable
@EqualsAndHashCode
public class GroupName {
    private final String groupName;

    protected GroupName() {
        this.groupName = null;
    }

    private GroupName(String groupName) {
        validateGroupName(groupName);
        this.groupName = groupName;
    }

    public static GroupName from(String groupName) {
        return new GroupName(groupName);
    }

    public String value() {
        return groupName;
    }

    private void validateGroupName(String groupName) {
        validateEmpty(groupName);
        validateLength(groupName);
        validateSpecialLetter(groupName);
    }

    private void validateEmpty(String groupName) {
        if (groupName == null || groupName.isBlank()) {
            throw new IllegalArgumentException("[ERROR] 그룹 이름은 비어 있을 수 없습니다.");
        }
    }

    private void validateLength(String groupName) {
        if (groupName.length() > 20) {
            throw new IllegalArgumentException("[ERROR] 그룹 이름의 길이는 20글자를 넘을 수 없습니다.");
        }
    }

    private void validateSpecialLetter(String groupName) {
        if (!groupName.chars().allMatch(Character::isAlphabetic)) {
            throw new IllegalArgumentException("[ERROR] 그룹 이름은 알파벳 혹은 한글로만 구성이 가능합니다.");
        }
    }

    @Override
    public String toString() {
        return groupName;
    }
}
