package org.noostak.server.appointment.application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.noostak.server.appointment.common.AppointmentErrorCode;
import org.noostak.server.appointment.common.AppointmentException;
import org.noostak.server.appointment.domain.*;
import org.noostak.server.appointment.domain.vo.AppointmentStatus;
import org.noostak.server.group.domain.Group;
import org.noostak.server.group.domain.GroupRepositoryTest;
import org.noostak.server.group.domain.vo.GroupImageUrl;
import org.noostak.server.group.domain.vo.GroupName;
import org.noostak.server.infra.MockFileStorageService;
import org.noostak.server.member.domain.Member;
import org.noostak.server.member.domain.MemberGroup;
import org.noostak.server.member.domain.MemberRepositoryTest;
import org.noostak.server.member.domain.vo.*;
import org.noostak.server.member.domain.vo.MemberGroupRepositoryTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
@DisplayName("약속 가능한 시간 저장 테스트")
public class AppointmentSaveAvailableTimesTest {

    private AppointmentMemberRepositoryTest appointmentMemberRepository;
    private AppointmentRepositoryTest appointmentRepository;
    private GroupRepositoryTest groupRepository;
    private MemberRepositoryTest memberRepository;
    private MemberGroupRepositoryTest memberGroupRepository;
    private MockFileStorageService fileStorageService;

    private Long savedMemberId;
    private Long savedGroupId;
    private Long savedAppointmentId;
    private Appointment appointment;
    private Member member;
    private Group group;
    private MockMultipartFile file;
    private AppointmentMember appointmentMember;

    @BeforeEach
    void setUp() {
        appointmentMemberRepository = new AppointmentMemberRepositoryTest();
        appointmentRepository = new AppointmentRepositoryTest();
        groupRepository = new GroupRepositoryTest();
        memberRepository = new MemberRepositoryTest();
        memberGroupRepository = new MemberGroupRepositoryTest();
        fileStorageService = new MockFileStorageService();

        member = createDefaultMember();
        file = createDefaultMultipartFile();

        group = saveGroup(member.getMemberId(), "Group 1", file);
        savedGroupId = group.getGroupId();
        savedMemberId = member.getMemberId();

        saveMemberGroup(member, group);

        appointment = appointmentRepository.save(Appointment.of(
                member, group, List.of(), "5차 회의", 120, "중요", AppointmentStatus.PROGRESS));
        appointmentMember = AppointmentMember.of(appointment, member, List.of());
        appointmentMemberRepository.save(appointmentMember);

        savedAppointmentId = appointment.getId();
    }

    @Nested
    @DisplayName("성공 케이스")
    class SuccessCases {

        @Test
        @DisplayName("약속 가능한 시간 저장 - 성공")
        void shouldSaveAvailableTimesSuccessfully() {
            // Given
            validateMemberInGroup(savedMemberId, savedGroupId);
            Appointment appointment = findValidAppointment(savedAppointmentId, savedGroupId);
            Member member = findValidMember(savedMemberId);
            AppointmentMember appointmentMember = findOrCreateAppointmentMember(appointment, member);
            List<AppointmentMemberDateTime> availableTimes = createAvailableTimes();
            List<AppointmentMemberDateTime> existingTimes = new ArrayList<>(appointmentMember.getAppointmentMemberDateTimes());
            List<AppointmentMemberDateTime> timesToAdd = findTimesToAdd(existingTimes, availableTimes);
            List<AppointmentMemberDateTime> timesToRemove = findTimesToRemove(existingTimes, availableTimes);

            // When
            updateTimes(existingTimes, timesToAdd, timesToRemove);

            // Then
            assertThat(existingTimes).hasSize(2);
            assertThat(existingTimes).containsAll(availableTimes);
        }
    }

    @Nested
    @DisplayName("실패 케이스")
    class FailureCases {

        @Test
        @DisplayName("회원이 그룹에 속하지 않은 경우")
        void shouldFailWhenMemberNotInGroup() {
            // Given
            Long invalidGroupId = savedGroupId + 1;

            // When & Then
            AppointmentException exception = assertThrows(AppointmentException.class, () -> {
                validateMemberInGroup(savedMemberId, invalidGroupId);
            });

            assertThat(exception.getErrorCode()).isEqualTo(AppointmentErrorCode.MEMBER_NOT_IN_GROUP);
        }

        @Test
        @DisplayName("약속이 존재하지 않는 경우")
        void shouldFailWhenAppointmentNotFound() {
            // Given
            Long invalidAppointmentId = savedAppointmentId + 1;

            // When & Then
            AppointmentException exception = assertThrows(AppointmentException.class, () -> {
                findAppointment(invalidAppointmentId, savedGroupId);
            });

            assertThat(exception.getErrorCode()).isEqualTo(AppointmentErrorCode.APPOINTMENT_NOT_FOUND);
        }

        @Test
        @DisplayName("약속이 그룹에 속하지 않은 경우")
        void shouldFailWhenAppointmentNotInGroup() {
            // Given
            Group differentGroup = groupRepository.save(
                    Group.of(
                            member.getMemberId(),
                            GroupName.from("다른 그룹"),
                            GroupImageUrl.from("https://example.com/different-group.jpg"),
                            "DIF123"
                    )
            );

            Appointment invalidAppointment = appointmentRepository.save(
                    Appointment.of(
                            member, differentGroup, List.of(), "5차 회의", 120, "중요", AppointmentStatus.PROGRESS
                    )
            );

            // When & Then
            AppointmentException exception = assertThrows(AppointmentException.class, () -> {
                findAppointment(invalidAppointment.getId(), savedGroupId);
            });

            assertThat(exception.getErrorCode()).isEqualTo(AppointmentErrorCode.APPOINTMENT_NOT_IN_GROUP);
        }

        @Test
        @DisplayName("회원이 존재하지 않는 경우")
        void shouldFailWhenMemberNotFound() {
            // Given
            Long invalidMemberId = savedMemberId + 1;

            // When & Then
            AppointmentException exception = assertThrows(AppointmentException.class, () -> {
                findById(invalidMemberId);
            });

            assertThat(exception.getErrorCode()).isEqualTo(AppointmentErrorCode.MEMBER_NOT_FOUND);
        }
    }

    private void validateMemberInGroup(Long memberId, Long groupId) {
        if (!memberGroupRepository.existsByGroupGroupIdAndMemberMemberId(memberId, groupId)) {
            throw new AppointmentException(AppointmentErrorCode.MEMBER_NOT_IN_GROUP);
        }
    }

    private Appointment findValidAppointment(Long appointmentId, Long groupId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new AppointmentException(AppointmentErrorCode.APPOINTMENT_NOT_FOUND));

        if (!appointment.getGroup().getGroupId().equals(groupId)) {
            throw new AppointmentException(AppointmentErrorCode.APPOINTMENT_NOT_IN_GROUP);
        }
        return appointment;
    }

    private Member findValidMember(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new AppointmentException(AppointmentErrorCode.MEMBER_NOT_FOUND));
    }

    private AppointmentMember findOrCreateAppointmentMember(Appointment appointment, Member member) {
        return appointmentMemberRepository.findByAppointmentAndMember(appointment, member)
                .orElseGet(() -> {
                    AppointmentMember newMember = AppointmentMember.of(appointment, member, List.of());
                    return appointmentMemberRepository.save(newMember);
                });
    }

    private List<AppointmentMemberDateTime> createAvailableTimes() {
        return List.of(
                AppointmentMemberDateTime.of(
                        LocalDateTime.now(),
                        LocalDateTime.now().withHour(10).withMinute(0),
                        LocalDateTime.now().withHour(12).withMinute(0)
                ),
                AppointmentMemberDateTime.of(
                        LocalDateTime.now().plusDays(1).withHour(14).withMinute(0),
                        LocalDateTime.now().plusDays(1).withHour(16).withMinute(0),
                        LocalDateTime.now().plusDays(1).withHour(18).withMinute(0)
                )
        );
    }

    private List<AppointmentMemberDateTime> findTimesToAdd(List<AppointmentMemberDateTime> existingTimes, List<AppointmentMemberDateTime> availableTimes) {
        Set<AppointmentMemberDateTime> existingSet = new HashSet<>(existingTimes);
        return availableTimes.stream()
                .filter(time -> !existingSet.contains(time))
                .toList();
    }

    private List<AppointmentMemberDateTime> findTimesToRemove(List<AppointmentMemberDateTime> existingTimes, List<AppointmentMemberDateTime> availableTimes) {
        return existingTimes.stream()
                .filter(time -> !availableTimes.contains(time))
                .toList();
    }

    private void updateTimes(List<AppointmentMemberDateTime> existingTimes, List<AppointmentMemberDateTime> timesToAdd, List<AppointmentMemberDateTime> timesToRemove) {
        existingTimes.removeAll(timesToRemove);
        existingTimes.addAll(timesToAdd);
    }

    private Appointment findAppointment(Long appointmentId, Long groupId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new AppointmentException(AppointmentErrorCode.APPOINTMENT_NOT_FOUND));

        if (!appointment.getGroup().getGroupId().equals(groupId)) {
            throw new AppointmentException(AppointmentErrorCode.APPOINTMENT_NOT_IN_GROUP);
        }

        return appointment;
    }

    private Member findById(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new AppointmentException(AppointmentErrorCode.MEMBER_NOT_FOUND));
    }

    private void saveMemberGroup(Member member, Group group) {
        MemberGroup memberGroup = MemberGroup.of(member, group);
        memberGroupRepository.save(memberGroup);
    }


    private Group saveGroup(Long hostId, String groupName, MockMultipartFile file) {
        String uploadedImageUrl = fileStorageService.uploadImage("group-images", file);
        return groupRepository.save(
                Group.of(
                        hostId,
                        GroupName.from(groupName),
                        GroupImageUrl.from(uploadedImageUrl),
                        "ABC123"
                )
        );
    }

    private MockMultipartFile createDefaultMultipartFile() {
        return new MockMultipartFile(
                "mockFile",
                "group-test.png",
                MediaType.IMAGE_PNG_VALUE,
                "test content".getBytes()
        );
    }

    private Member createDefaultMember() {
        return saveMember("firstMember", "https://noostak.s3.ap-northeast-2.amazonaws.com/group-images/1.jpg", "123456", "refreshToken1");
    }

    private Member saveMember(String name, String imageUrl, String socialId, String refreshToken) {
        return memberRepository.save(
                Member.of(
                        MemberName.from(name),
                        ProfileImageUrl.from(imageUrl),
                        AccountStatus.ACTIVE,
                        SocialType.GOOGLE,
                        SocialId.from(socialId),
                        refreshToken
                )
        );
    }
}
