package org.noostak.server.like.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.noostak.server.appointment.domain.AppointmentMember;
import org.noostak.server.global.entity.BaseTimeEntity;
import org.noostak.server.like.domain.vo.LikesCount;
import org.noostak.server.appointment.domain.Option;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "likes")
public class Like extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long likeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "option_id", nullable = false)
    private Option option;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appointment_members_id", nullable = false)
    private AppointmentMember appointmentMember;

    @Embedded
    @AttributeOverride(name = "count", column = @Column(name = "likes_count"))
    private LikesCount likesCount;
}

