package org.noostak.server.member.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.noostak.server.appointment.domain.AppointmentMember;
import org.noostak.server.global.entity.BaseTimeEntity;
import org.noostak.server.member.domain.vo.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@RequiredArgsConstructor
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<MemberGroup> memberGroups = new HashSet<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<AppointmentMember> appointmentMember = new HashSet<>();

    @Embedded
    @AttributeOverride(name = "memberName", column = @Column(name = "member_name"))
    private MemberName name;

    @Embedded
    @AttributeOverride(name = "url", column = @Column(name = "profile_url"))
    private ProfileImageUrl url;

    @Enumerated(EnumType.STRING)
    private AccountStatus accountStatus;

    @Enumerated(EnumType.STRING)
    private SocialType socialLoginType;

    @Embedded
    private SocialId socialId;

    private String refreshToken;

    private Member(final MemberName name, final ProfileImageUrl url, final AccountStatus accountStatus, final SocialType socialLoginType, final SocialId socialId, final String refreshToken) {
        this.name = name;
        this.url = url;
        this.accountStatus = AccountStatus.ACTIVE;
        this.socialLoginType = socialLoginType;
        this.socialId = socialId;
        this.refreshToken = refreshToken;
    }

    public static Member of(final MemberName name, final ProfileImageUrl url, final AccountStatus accountStatus, final SocialType socialLoginType, final SocialId socialId, final String refreshToken) {
        return new Member(name, url, accountStatus,socialLoginType, socialId, refreshToken);
    }
}
