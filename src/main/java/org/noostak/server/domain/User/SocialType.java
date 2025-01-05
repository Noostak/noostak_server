package org.noostak.server.domain.User;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SocialType {
    KAKAO("카카오"),
    GOOGLE("구글"),
    APPLE("애플");

    private final String message;
}
