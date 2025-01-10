package org.noostak.server.like.domain.vo;

import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;

@Embeddable
@EqualsAndHashCode
public class LikesCount {

    private static final Long MAX_LIKES = 50L;

    private final Long count;

    protected LikesCount() {
        this.count = 0L;
    }

    private LikesCount(Long count) {
        validate(count);
        this.count = count;
    }

    public static LikesCount from(Long count) {
        return new LikesCount(count);
    }

    public Long value() {
        return count;
    }

    public LikesCount increase() {
        Long updatedCount = this.count + 1;
        validate(updatedCount);
        return new LikesCount(updatedCount);
    }

    public LikesCount decrease() {
        Long updatedCount = this.count - 1;
        validate(updatedCount);
        return new LikesCount(updatedCount);
    }

    private void validate(Long count) {
        validateNonNegative(count);
        validateMaxLimit(count);
    }

    private void validateNonNegative(Long count) {
        if (count < 0) {
            throw new IllegalArgumentException("[ERROR] 좋아요 수는 음수가 될 수 없습니다.");
        }
    }

    private void validateMaxLimit(Long count) {
        if (count > MAX_LIKES) {
            throw new IllegalArgumentException("[ERROR] 좋아요 수는 최대 " + MAX_LIKES + "를 초과할 수 없습니다.");
        }
    }

    @Override
    public String toString() {
        return String.valueOf(count);
    }
}
