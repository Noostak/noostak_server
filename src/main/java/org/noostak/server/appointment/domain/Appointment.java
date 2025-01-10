package org.noostak.server.appointment.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.noostak.server.appointment.domain.vo.AppointmentName;
import org.noostak.server.appointment.domain.vo.AppointmentStatus;
import org.noostak.server.global.entity.BaseTimeEntity;
import org.noostak.server.member.domain.Member;
import org.noostak.server.group.domain.Group;

import java.time.LocalDateTime;

@Entity
@Getter
@RequiredArgsConstructor
public class Appointment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", nullable = false)
    private Group group;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "host_id", nullable = false)
    private Member host;

    @Embedded
    @AttributeOverride(name = "appointmentName", column = @Column(name = "appointment_name", nullable = false))
    private AppointmentName name;

    @Enumerated(EnumType.STRING)
    private AppointmentStatus appointmentStatus;

    private LocalDateTime date;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

}
