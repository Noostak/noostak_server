package org.noostak.server.domain.User;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.noostak.server.domain.appointment.Appointment;
import org.noostak.server.domain.group.Group;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@RequiredArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id")
    private Group group;

    // TODO: 모든 연관관계 orphanRemoval 추가할건지
    @OneToMany(mappedBy = "host", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Appointment> hostedAppointments = new HashSet<>();

    @Embedded
    @AttributeOverride(name = "userName", column = @Column(name = "user_name", nullable = false))
    private UserName name;

    @Embedded
    @AttributeOverride(name = "url", column = @Column(name = "profile_url"))
    private ProfileImageUrl url;

    @Column(nullable = false)
    private String socialId;

    private String refreshToken;

    @Enumerated(EnumType.STRING)
    private SocialType socialLoginType;

    @Enumerated(EnumType.STRING)
    private AccountStatus accountStatus;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime modifiedAt;
}
