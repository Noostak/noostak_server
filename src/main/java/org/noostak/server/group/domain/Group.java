package org.noostak.server.group.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.noostak.server.appointment.domain.Appointment;
import org.noostak.server.global.entity.BaseTimeEntity;
import org.noostak.server.member.domain.Member;
import org.noostak.server.member.domain.MemberGroup;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@RequiredArgsConstructor
public class Group extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long groupId;

    @Embedded
    @AttributeOverride(name = "groupName", column = @Column(name = "group_name", nullable = false))
    private GroupName name;

    @Embedded
    private InviteCodes inviteCodes = InviteCodes.init();

    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<MemberGroup> memberGroups = new HashSet<>();

    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Appointment> appointments = new HashSet<>();
}

