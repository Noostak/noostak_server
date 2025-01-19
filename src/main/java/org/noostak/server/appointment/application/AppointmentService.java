package org.noostak.server.appointment.application;

import lombok.RequiredArgsConstructor;
import org.noostak.server.appointment.common.AppointmentErrorCode;
import org.noostak.server.appointment.common.AppointmentException;
import org.noostak.server.appointment.domain.*;
import org.noostak.server.appointment.domain.repository.AppointmentMemberRepository;
import org.noostak.server.appointment.domain.repository.AppointmentRepository;
import org.noostak.server.appointment.domain.vo.AppointmentStatus;
import org.noostak.server.appointment.dto.request.AppointmentCreateRequest;
import org.noostak.server.appointment.dto.request.AvailableTimesRequest;
import org.noostak.server.appointment.dto.response.AppointmentCreateResponse;
import org.noostak.server.appointment.dto.response.AppointmentDateTimeResponse;
import org.noostak.server.group.domain.Group;
import org.noostak.server.group.domain.GroupRepository;
import org.noostak.server.member.domain.Member;
import org.noostak.server.member.domain.MemberRepository;
import org.noostak.server.member.domain.vo.MemberGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final AppointmentMemberRepository appointmentMemberRepository;
    private final GroupRepository groupRepository;
    private final MemberRepository memberRepository;
    private final MemberGroupRepository memberGroupRepository;

    @Transactional
    public AppointmentCreateResponse createAppointment(Long userId, Long groupId, AppointmentCreateRequest request) {
        Member host = findById(userId);
        Group group = findGroupById(groupId);
        List<AppointmentDateTime> appointmentDateTimes = request.appointmentDateTimes();
        Appointment appointment = createAppointmentEntity(host, group, appointmentDateTimes, request);
        saveAppointment(appointment);
        return buildAppointmentCreateResponse(appointment, appointmentDateTimes);
    }

    @Transactional
    public void saveAvailableTimes(Long memberId, Long groupId, Long appointmentId, AvailableTimesRequest request) {
        validateMemberInGroup(memberId, groupId);
        Appointment appointment = findAppointment(appointmentId, groupId);
        Member member = findById(memberId);
        AppointmentMember appointmentMember = findOrCreateAppointmentMember(appointment, member);
        List<AppointmentMemberDateTime> availableTimes = request.availableTimes();
        updateAppointmentMemberTimes(appointmentMember, availableTimes);
    }

    private void validateMemberInGroup(Long memberId, Long groupId) {
        if (!memberGroupRepository.existsByGroupGroupIdAndMemberMemberId(memberId, groupId)) {
            throw new AppointmentException(AppointmentErrorCode.MEMBER_NOT_IN_GROUP);
        }
    }

    private Appointment findAppointment(Long appointmentId, Long groupId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new AppointmentException(AppointmentErrorCode.APPOINTMENT_NOT_FOUND));

        if (!appointment.getGroup().getGroupId().equals(groupId)) {
            throw new AppointmentException(AppointmentErrorCode.APPOINTMENT_NOT_IN_GROUP);
        }

        return appointment;
    }

    private AppointmentMember findOrCreateAppointmentMember(Appointment appointment, Member member) {
        return appointmentMemberRepository.findByAppointmentAndMember(appointment, member)
                .orElseGet(() -> {
                    AppointmentMember newMember = AppointmentMember.of(
                            appointment,
                            member,
                            List.of()
                    );
                    return appointmentMemberRepository.save(newMember);
                });
    }

    private void updateAppointmentMemberTimes(AppointmentMember appointmentMember, List<AppointmentMemberDateTime> availableTimes) {
        List<AppointmentMemberDateTime> existingTimes = appointmentMember.getAppointmentMemberDateTimes();

        Set<AppointmentMemberDateTime> existingSet = new HashSet<>(existingTimes);
        List<AppointmentMemberDateTime> timesToAdd = availableTimes.stream()
                .filter(time -> !existingSet.contains(time))
                .toList();

        List<AppointmentMemberDateTime> timesToRemove = existingTimes.stream()
                .filter(time -> !availableTimes.contains(time))
                .toList();

        existingTimes.removeAll(timesToRemove);
        existingTimes.addAll(timesToAdd);
    }

    private Member findById(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new AppointmentException(AppointmentErrorCode.MEMBER_NOT_FOUND));
    }

    private Group findGroupById(Long groupId) {
        return groupRepository.findById(groupId)
                .orElseThrow(() -> new AppointmentException(AppointmentErrorCode.GROUP_NOT_FOUND));
    }

    private Appointment createAppointmentEntity(Member host, Group group, List<AppointmentDateTime> appointmentDateTimes, AppointmentCreateRequest request) {
        return Appointment.of(
                host,
                group,
                appointmentDateTimes,
                request.appointmentName(),
                request.duration(),
                request.category(),
                AppointmentStatus.PROGRESS
        );
    }

    private void saveAppointment(Appointment appointment) {
        appointmentRepository.save(appointment);
    }

    private AppointmentCreateResponse buildAppointmentCreateResponse(Appointment appointment, List<AppointmentDateTime> appointmentDateTimes) {
        List<AppointmentDateTimeResponse> dateTimeResponses = createAppointmentDateTimeResponses(appointmentDateTimes);
        return createAppointmentCreateResponse(appointment, dateTimeResponses);
    }

    private List<AppointmentDateTimeResponse> createAppointmentDateTimeResponses(List<AppointmentDateTime> appointmentDateTimes) {
        return appointmentDateTimes.stream()
                .map(dateTime -> AppointmentDateTimeResponse.of(
                        dateTime.getDate(),
                        dateTime.getStartTime(),
                        dateTime.getEndTime()
                ))
                .collect(Collectors.toList());
    }

    private AppointmentCreateResponse createAppointmentCreateResponse(Appointment appointment, List<AppointmentDateTimeResponse> dateTimeResponses) {
        return AppointmentCreateResponse.of(
                appointment.getName().value(),
                appointment.getCategory().getMessage(),
                appointment.getDuration().value(),
                dateTimeResponses
        );
    }
}
