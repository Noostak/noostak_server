package org.noostak.server.group.domain;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class DefaultInviteCodePolicy implements InviteCodePolicy {

    private static final int DEFAULT_LENGTH = 6;
    private static final String DEFAULT_CHARACTERS = generateDefaultCharacters();

    private static String generateDefaultCharacters() {
        return IntStream.concat(
                        IntStream.rangeClosed('A', 'Z'),
                        IntStream.rangeClosed('0', '9')
                )
                .mapToObj(ch -> String.valueOf((char) ch))
                .collect(Collectors.joining());
    }

    @Override
    public int codeLength() {
        return DEFAULT_LENGTH;
    }

    @Override
    public String allowedCharacters() {
        return DEFAULT_CHARACTERS;
    }

    @Override
    public boolean validate(String code) {
        return code.length() == DEFAULT_LENGTH &&
                code.chars().allMatch(ch -> DEFAULT_CHARACTERS.indexOf(ch) >= 0);
    }
}
