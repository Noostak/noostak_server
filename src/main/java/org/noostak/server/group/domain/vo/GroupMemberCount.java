package org.noostak.server.group.domain.vo;

import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import org.noostak.server.group.common.GroupErrorCode;
import org.noostak.server.group.common.GroupException;

@Embeddable
@EqualsAndHashCode
public class GroupMemberCount {

    private static final Long MAX_MEMBERS = 50L;

    private final Long count;

    protected GroupMemberCount() {
        this.count = 0L;
    }

    private GroupMemberCount(Long count) {
        validateChange(count);
        this.count = count;
    }

    public static GroupMemberCount from(Long count) {
        validateInitial(count);
        return new GroupMemberCount(count);
    }

    public Long value() {
        return count;
    }

    public GroupMemberCount increase() {
        Long updatedCount = this.count + 1;
        validateChange(updatedCount);
        return new GroupMemberCount(updatedCount);
    }

    public GroupMemberCount decrease() {
        Long updatedCount = this.count - 1;
        validateChange(updatedCount);
        return new GroupMemberCount(updatedCount);
    }

    private static void validateInitial(Long count) {
        if (count < 0) {
            throw new GroupException(GroupErrorCode.MEMBER_COUNT_INITIAL_NEGATIVE);
        }
    }

    private static void validateChange(Long count) {
        validateNonNegative(count);
        validateMaxLimit(count);
    }

    private static void validateNonNegative(Long count) {
        if (count < 0) {
            throw new GroupException(GroupErrorCode.MEMBER_COUNT_NEGATIVE);
        }
    }

    private static void validateMaxLimit(Long count) {
        if (count > MAX_MEMBERS) {
            throw new GroupException(GroupErrorCode.MEMBER_COUNT_EXCEEDS_MAX_LIMIT);
        }
    }

    @Override
    public String toString() {
        return String.valueOf(count);
    }
}
