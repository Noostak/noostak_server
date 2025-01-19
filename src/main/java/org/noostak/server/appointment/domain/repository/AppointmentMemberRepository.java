package org.noostak.server.appointment.domain.repository;

import org.noostak.server.appointment.domain.Appointment;
import org.noostak.server.appointment.domain.AppointmentMember;
import org.noostak.server.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AppointmentMemberRepository extends JpaRepository<AppointmentMember, Long> {
    Optional<AppointmentMember> findByAppointmentAndMember(Appointment appointment, Member member);
}
