package org.noostak.server.member.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.noostak.server.group.domain.Group;

@Entity
@Getter
@NoArgsConstructor
public class MemberGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberGroupId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id")
    private Group group;

    private MemberGroup(final Member member, final Group group) {
        this.member = member;
        this.group = group;
    }

    public static MemberGroup of(final Member member, final Group group) {
        return new MemberGroup(member, group);
    }
}
