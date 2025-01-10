package org.noostak.server.member.domain.vo;

import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;

@Embeddable
@EqualsAndHashCode
public class ProfileImageUrl {

    private final String url;

    protected ProfileImageUrl() {
        this.url = null;
    }

    private ProfileImageUrl(String url) {
        this.url = url;
    }

    public static ProfileImageUrl from(String url) {
        if (url == null) {
            return new ProfileImageUrl();
        }
        validate(url);
        return new ProfileImageUrl(url);
    }

    public String value() {
        return url;
    }

    private static void validate(String url) {
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
