package org.noostak.server.appointment.application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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

@Transactional
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

    private static final String DEFAULT_MEMBER_NAME = "firstMember";
    private static final String DEFAULT_IMAGE_URL = "https://noostak.s3.ap-northeast-2.amazonaws.com/group-images/1.jpg";
    private static final String DEFAULT_SOCIAL_ID = "123456";
    private static final String DEFAULT_REFRESH_TOKEN = "refreshToken1";
    private static final String DEFAULT_GROUP_NAME = "Group 1";
    private static final String DEFAULT_FILE_NAME = "group-thumbnail.png";
    private static final String DEFAULT_FILE_PARAM_NAME = "file";
    private static final String DEFAULT_FILE_CONTENT = "thumbnail content";
    private static final String DEFAULT_FILE_PATH = "group-images";
    private static final String DEFAULT_GROUP_CODE = "ABC123";
    private static final String TEST_APPOINTMENT_NAME = "5차 회의";
    private static final String TEST_CATEGORY = "중요";
    private static final int TEST_DURATION = 120;


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


        group = saveGroup(member.getMemberId(), DEFAULT_GROUP_NAME, file);
        savedGroupId = group.getGroupId();
        savedMemberId = member.getMemberId();


        saveMemberGroup(member, group);


        appointment = appointmentRepository.save(Appointment.of(
                member, group, List.of(), TEST_APPOINTMENT_NAME, TEST_DURATION, TEST_CATEGORY, AppointmentStatus.PROGRESS));


        appointmentMember = AppointmentMember.of(appointment, member, List.of());


        appointmentMemberRepository.save(appointmentMember);

        savedAppointmentId = appointment.getId();
    }



    @Test
    @DisplayName("saveAvailableTimes - 성공")
    void shouldSaveAvailableTimesSuccessfully() {
        // Given

        if (!memberGroupRepository.existsByGroupGroupIdAndMemberMemberId(savedMemberId, savedGroupId)) {
            throw new AppointmentException(AppointmentErrorCode.MEMBER_NOT_IN_GROUP);
        }


        Appointment appointment = appointmentRepository.findById(savedAppointmentId)
                .orElseThrow(() -> new AppointmentException(AppointmentErrorCode.APPOINTMENT_NOT_FOUND));
        if (!appointment.getGroup().getGroupId().equals(savedGroupId)) {
            throw new AppointmentException(AppointmentErrorCode.APPOINTMENT_NOT_IN_GROUP);
        }


        Member member = memberRepository.findById(savedMemberId)
                .orElseThrow(() -> new AppointmentException(AppointmentErrorCode.MEMBER_NOT_FOUND));


        AppointmentMember appointmentMember = appointmentMemberRepository.findByAppointmentAndMember(appointment, member)
                .orElseGet(() -> {
                    AppointmentMember newMember = AppointmentMember.of(appointment, member, List.of());
                    return appointmentMemberRepository.save(newMember);
                });


        List<AppointmentMemberDateTime> availableTimes = List.of(
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


        List<AppointmentMemberDateTime> existingTimes = new ArrayList<>(appointmentMember.getAppointmentMemberDateTimes());


        Set<AppointmentMemberDateTime> existingSet = new HashSet<>(existingTimes);
        List<AppointmentMemberDateTime> timesToAdd = availableTimes.stream()
                .filter(time -> !existingSet.contains(time))
                .toList();
        List<AppointmentMemberDateTime> timesToRemove = existingTimes.stream()
                .filter(time -> !availableTimes.contains(time))
                .toList();

        existingTimes.removeAll(timesToRemove);
        existingTimes.addAll(timesToAdd);

        // Then
        assertThat(existingTimes).hasSize(2);
        assertThat(existingTimes).containsAll(availableTimes);
    }

    private void saveMemberGroup(Member member, Group group) {
        MemberGroup memberGroup = MemberGroup.of(member, group);
        memberGroupRepository.save(memberGroup);
    }


    private Group saveGroup(Long hostId, String groupName, MockMultipartFile file) {
        String uploadedImageUrl = fileStorageService.uploadImage(DEFAULT_FILE_PATH, file);
        return groupRepository.save(
                Group.of(
                        hostId,
                        GroupName.from(groupName),
                        GroupImageUrl.from(uploadedImageUrl),
                        DEFAULT_GROUP_CODE
                )
        );
    }

    private MockMultipartFile createDefaultMultipartFile() {
        return new MockMultipartFile(
                DEFAULT_FILE_PARAM_NAME,
                DEFAULT_FILE_NAME,
                MediaType.IMAGE_PNG_VALUE,
                DEFAULT_FILE_CONTENT.getBytes()
        );
    }

    private Member createDefaultMember() {
        return saveMember(DEFAULT_MEMBER_NAME, DEFAULT_IMAGE_URL, DEFAULT_SOCIAL_ID, DEFAULT_REFRESH_TOKEN);
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
