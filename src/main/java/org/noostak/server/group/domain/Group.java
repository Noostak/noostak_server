package org.noostak.server.group.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.noostak.server.appointment.domain.Appointment;
import org.noostak.server.global.entity.BaseTimeEntity;
import org.noostak.server.group.domain.vo.GroupImageUrl;
import org.noostak.server.group.domain.vo.GroupMemberCount;
import org.noostak.server.group.domain.vo.GroupName;
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

//    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
//    @JoinColumn(name = "group_invite_code_id")
//    private GroupInviteCode groupInviteCode;

    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<MemberGroup> members = new HashSet<>();

    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Appointment> appointments = new HashSet<>();

    @Embedded
    @AttributeOverride(name = "groupName", column = @Column(name = "group_name"))
    private GroupName name;

    @Embedded
    @AttributeOverride(name = "url", column = @Column(name = "group_image_url"))
    private GroupImageUrl url;

    @Embedded
    @AttributeOverride(name = "count", column = @Column(name = "member_count"))
    private GroupMemberCount memberCount;

//    public void addMember() {
//        this.memberCount = memberCount.increase();
//    }
//
//    public void removeMember() {
//        this.memberCount = memberCount.decrease();
//    }
}

