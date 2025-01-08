package org.noostak.server.member.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.noostak.server.appointment.domain.Appointment;
import org.noostak.server.global.common.BaseTimeEntity;
import org.noostak.server.group.domain.Group;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@RequiredArgsConstructor
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;

    // TODO: 모든 연관관계 orphanRemoval 추가할건지
    @OneToMany(mappedBy = "host", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Appointment> hostedAppointments = new HashSet<>();

    @Embedded
    @AttributeOverride(name = "memberName", column = @Column(name = "member_name", nullable = false))
    private MemberName name;

    @Embedded
    @AttributeOverride(name = "url", column = @Column(name = "profile_url"))
    private ProfileImageUrl url;

    @Enumerated(EnumType.STRING)
    private SocialType socialLoginType;

    @Column(nullable = false)
    private String socialId;

    @Enumerated(EnumType.STRING)
    private AccountStatus accountStatus;

    private String refreshToken;

}
