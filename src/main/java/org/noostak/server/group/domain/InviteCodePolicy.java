package org.noostak.server.group.domain;

public interface InviteCodePolicy {
    int codeLength();
    String allowedCharacters();
    boolean validate(String code);
}
