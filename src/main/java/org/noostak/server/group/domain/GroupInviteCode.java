package org.noostak.server.group.domain;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.noostak.server.group.domain.vo.Code;

@Entity
@Getter
@NoArgsConstructor
@EqualsAndHashCode
public class GroupInviteCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private Code code;

    private GroupInviteCode(Code code) {
        this.code = code;
    }

    public static GroupInviteCode from(String codeValue) {
        return new GroupInviteCode(Code.from(codeValue));
    }

    public String value() {
        return code.value();
    }
}
