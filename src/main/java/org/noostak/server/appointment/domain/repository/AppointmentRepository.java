package org.noostak.server.appointment.domain.repository;

import org.noostak.server.appointment.domain.Appointment;
import org.noostak.server.appointment.domain.vo.AppointmentStatus;
import org.noostak.server.group.domain.Group;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findByGroupAndAppointmentStatus(Group group, AppointmentStatus status);
}
