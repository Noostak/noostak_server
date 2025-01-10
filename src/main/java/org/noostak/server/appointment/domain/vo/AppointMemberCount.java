package org.noostak.server.appointment.domain.vo;

import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;

@Embeddable
@EqualsAndHashCode
public class AppointMemberCount {

    private static final Long MAX_MEMBERS = 50L;

    private final Long count;

    protected AppointMemberCount() {
        this.count = 0L;
    }

    private AppointMemberCount(Long count) {
        validate(count);
        this.count = count;
    }

    public static AppointMemberCount from(Long count) {
        return new AppointMemberCount(count);
    }

    public Long value() {
        return count;
    }

    public AppointMemberCount increase() {
        Long updatedCount = this.count + 1;
        validate(updatedCount);
        return new AppointMemberCount(updatedCount);
    }

    public AppointMemberCount decrease() {
        Long updatedCount = this.count - 1;
        validate(updatedCount);
        return new AppointMemberCount(updatedCount);
    }

    private void validate(Long count) {
        validateNonNegative(count);
        validateMaxLimit(count);
    }

    private void validateNonNegative(Long count) {
        if (count < 0) {
            throw new IllegalArgumentException("[ERROR] 약속 멤버 수는 음수가 될 수 없습니다.");
        }
    }

    private void validateMaxLimit(Long count) {
        if (count > MAX_MEMBERS) {
            throw new IllegalArgumentException("[ERROR] 약속 멤버 수는 최대 " + MAX_MEMBERS + "명을 초과할 수 없습니다.");
        }
    }

    @Override
    public String toString() {
        return String.valueOf(count);
    }
}