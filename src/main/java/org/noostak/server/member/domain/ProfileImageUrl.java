package org.noostak.server.member.domain;

import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;

@Embeddable
@EqualsAndHashCode
public class ProfileImageUrl {

    private final String url;

    protected ProfileImageUrl() {
        this.url = null;
    }

    public ProfileImageUrl(String url) {
        validate(url);
        this.url = url;
    }

    public String value() {
        return url;
    }

    public static void validate(String url) {
        validateUrlFormat(url);
    }

    private static void validateUrlFormat(String url) {
        if (!url.matches("^(http|https)://.*$")) {
            throw new IllegalArgumentException("[ERROR] 프로필 이미지 URL은 유효한 URL이어야 합니다.");
        }
    }

    @Override
    public String toString() {
        return url;
    }
}
