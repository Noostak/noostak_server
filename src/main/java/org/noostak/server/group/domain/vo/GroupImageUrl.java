package org.noostak.server.group.domain.vo;

import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import org.noostak.server.group.common.GroupErrorCode;
import org.noostak.server.group.common.GroupException;

@Embeddable
@EqualsAndHashCode
public class GroupImageUrl {

    private final String url;

    protected GroupImageUrl() {
        this.url = null;
    }

    private GroupImageUrl(String url) {
        this.url = url;
    }

    public static GroupImageUrl from(String url) {
        if (url == null) {
            return new GroupImageUrl();
        }
        validate(url);
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
            throw new GroupException(GroupErrorCode.INVALID_GROUP_IMAGE_URL);
        }
    }

    @Override
    public String toString() {
        return url;
    }
}
