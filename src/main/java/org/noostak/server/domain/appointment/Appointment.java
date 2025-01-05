package org.noostak.server.domain.appointment;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.noostak.server.domain.User.User;
import org.noostak.server.domain.group.Group;

import java.time.LocalDateTime;

@Entity
@Getter
@RequiredArgsConstructor
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", nullable = false)
    private Group group;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "host_id", nullable = false)
    private User host;

    @Embedded
    @AttributeOverride(name = "appointmentName", column = @Column(name = "appointment_name", nullable = false))
    private AppointmentName name;

    @Enumerated(EnumType.STRING)
    private AppointmentStatus appointmentStatus;

    private Long likesCount;

    private LocalDateTime date;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime modifiedAt;

}
