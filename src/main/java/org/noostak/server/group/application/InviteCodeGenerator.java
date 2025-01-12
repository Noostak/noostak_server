package org.noostak.server.group.application;

import lombok.RequiredArgsConstructor;
import org.noostak.server.group.common.GroupErrorCode;
import org.noostak.server.group.common.GroupException;
import org.noostak.server.group.domain.GroupInviteCode;
import org.noostak.server.group.domain.InviteCodePolicy;

import java.security.SecureRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RequiredArgsConstructor
public class InviteCodeGenerator {

    private final SecureRandom random;
    private final InviteCodePolicy policy;

    public GroupInviteCode generate() {
        String randomCode = generateAlphaNumericCode(policy.codeLength());
        validateCode(randomCode);
        return GroupInviteCode.from(randomCode);
    }

    private String generateAlphaNumericCode(int length) {
        String allowedChars = policy.allowedCharacters();
        return IntStream.range(0, length)
                .map(i -> random.nextInt(allowedChars.length()))
                .mapToObj(allowedChars::charAt)
                .map(Object::toString)
                .collect(Collectors.joining());
    }

    private void validateCode(String code) {
        if (!policy.validate(code)) {
            throw new GroupException(GroupErrorCode.INVALID_INVITE_CODE);
        }
    }
}
