package org.noostak.server.group.domain.vo;

import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;

@Embeddable
@EqualsAndHashCode
public class GroupImageUrl {

    private final String url;

    protected GroupImageUrl() {
        this.url = null;
    }

    private GroupImageUrl(String url) {
        validate(url);
        this.url = url;
    }

    public static GroupImageUrl from(String url) {
        return new GroupImageUrl(url);
    }

    public String value() {
        return url;
    }

    public static void validate(String url) {
        validateUrlFormat(url);
    }

    private static void validateUrlFormat(String url) {
        if (!url.matches("^(http|https)://.*$")) {
            throw new IllegalArgumentException("[ERROR] 그룹 이미지 URL은 유효한 URL이어야 합니다.");
        }
    }

    @Override
    public String toString() {
        return url;
    }
}
