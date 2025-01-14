package org.noostak.server.group.domain.vo;

import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import org.noostak.server.group.common.GroupErrorCode;
import org.noostak.server.group.common.GroupException;

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
    }

    private void validateEmpty(String groupName) {
        if (groupName == null || groupName.isBlank()) {
            throw new GroupException(GroupErrorCode.GROUP_NAME_NOT_EMPTY);
        }
    }

    private void validateLength(String groupName) {
        if (groupName.length() > 30) {
            throw new GroupException(GroupErrorCode.INVALID_GROUP_NAME_LENGTH);
        }
    }

    @Override
    public String toString() {
        return groupName;
    }
}
