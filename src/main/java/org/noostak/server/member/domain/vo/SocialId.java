package org.noostak.server.member.domain.vo;

import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;

@Embeddable
@EqualsAndHashCode
public class SocialId {

    private final String value;

    protected SocialId() {
        this.value = null;
    }

    private SocialId(String value) {
        validate(value);
        this.value = value;
    }

    public static SocialId from(String value) {
        return new SocialId(value);
    }

    public String value() {
        return value;
    }

    private static void validate(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("[ERROR] 소셜 ID는 비어 있을 수 없습니다.");
        }
    }

    @Override
    public String toString() {
        return value;
    }
}
