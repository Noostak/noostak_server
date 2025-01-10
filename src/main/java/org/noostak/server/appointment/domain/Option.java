package org.noostak.server.appointment.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class Option {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long optionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appointment_id")
    private Appointment appointment;

    private LocalDateTime date;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    public void initAppointment(Appointment appointment) {
        validateAppointment(appointment);
        this.appointment = appointment;
    }

    public void clearAppointment() {
        validateClearableAppointment();
        this.appointment = null;
    }

    private void validateAppointment(Appointment appointment) {
        if (this.appointment != null) {
            throw new IllegalStateException("[ERROR] 연관된 Appointment가 존재합니다.");
        }
    }

    private void validateClearableAppointment() {
        if (this.appointment == null) {
            throw new IllegalStateException("[ERROR] 연관된 Appointment가 존재하지 않습니다.");
        }
    }

}
