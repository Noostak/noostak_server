package org.noostak.server.domain.group;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embeddable;
import jakarta.persistence.FetchType;
import lombok.EqualsAndHashCode;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Embeddable
@EqualsAndHashCode
public class InviteCodes {

    @ElementCollection(fetch = FetchType.EAGER)
    private final Set<InviteCode> codes = new HashSet<>();

    protected InviteCodes() {
    }

    private InviteCodes(Set<InviteCode> codes) {
        this.codes.addAll(codes);
    }

    public static InviteCodes init() {
        return new InviteCodes(new HashSet<>());
    }

    public static InviteCodes of(Set<String> codes) {
        Set<InviteCode> inviteCodes = codes.stream()
                .map(InviteCode::from)
                .collect(Collectors.toSet());
        return new InviteCodes(inviteCodes);
    }

    public boolean contains(String code) {
        return codes.contains(InviteCode.from(code));
    }

    public void add(String code) {
        InviteCode inviteCode = InviteCode.from(code);
        if (codes.contains(inviteCode)) {
            throw new IllegalArgumentException("[ERROR] 중복된 초대 코드입니다.");
        }
        codes.add(inviteCode);
    }

    public void remove(String code) {
        codes.remove(InviteCode.from(code));
    }

    public Set<InviteCode> getCodes() {
        return Collections.unmodifiableSet(codes);
    }
}
