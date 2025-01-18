package org.noostak.server.appointment.application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.noostak.server.appointment.common.AppointmentErrorCode;
import org.noostak.server.appointment.common.AppointmentException;
import org.noostak.server.appointment.domain.Appointment;
import org.noostak.server.appointment.domain.AppointmentDateTime;
import org.noostak.server.appointment.domain.AppointmentRepositoryTest;
import org.noostak.server.appointment.domain.vo.AppointmentStatus;
import org.noostak.server.appointment.dto.request.AppointmentCreateRequest;
import org.noostak.server.appointment.dto.response.AppointmentCreateResponse;
import org.noostak.server.appointment.dto.response.AppointmentDateTimeResponse;
import org.noostak.server.group.domain.Group;
import org.noostak.server.group.domain.GroupRepositoryTest;
import org.noostak.server.group.domain.vo.GroupImageUrl;
import org.noostak.server.group.domain.vo.GroupName;
import org.noostak.server.infra.MockFileStorageService;
import org.noostak.server.member.domain.Member;
import org.noostak.server.member.domain.MemberRepositoryTest;
import org.noostak.server.member.domain.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@Transactional
class AppointmentCreateServiceTest {

    private AppointmentRepositoryTest appointmentRepository;
    private GroupRepositoryTest groupRepository;
    private MemberRepositoryTest memberRepository;
    private MockFileStorageService fileStorageService;

    private Long savedGroupId;
    private Long savedMemberId;

    @BeforeEach
    void setUp() {

        appointmentRepository = new AppointmentRepositoryTest();
        groupRepository = new GroupRepositoryTest();
        memberRepository = new MemberRepositoryTest();
        fileStorageService = new MockFileStorageService();

        appointmentRepository.deleteAll();
        groupRepository.deleteAll();
        memberRepository.deleteAll();

        Member savedMember = saveMember("firstMember",
                "https://noostak.s3.ap-northeast-2.amazonaws.com/group-images/1.jpg",
                "123456",
                "refreshToken1");

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "group-thumbnail.png",
                MediaType.IMAGE_PNG_VALUE,
                "thumbnail content".getBytes()
        );
        savedMemberId = savedMember.getMemberId();
        Group savedGroup = saveGroup(savedMemberId, "Group 1", file);
        savedGroupId = savedGroup.getGroupId();

    }

    @Nested
    @DisplayName("성공 케이스")
    class Success {

        @Test
        @DisplayName("약속 생성 성공")
        void shouldCreateAppointment() {
            // Given
            String appointmentName = "Study Group";
            String category = "중요";
            Integer duration = 120;
            List<AppointmentDateTime> dateTimeRequests = List.of(
                    AppointmentDateTime.of(
                            LocalDateTime.now(),
                            LocalDateTime.now().withHour(10).withMinute(0),
                            LocalDateTime.now().withHour(12).withMinute(0)
                    ),
                    AppointmentDateTime.of(
                            LocalDateTime.now().plusDays(1).withHour(14).withMinute(0),
                            LocalDateTime.now().plusDays(1).withHour(16).withMinute(0),
                            LocalDateTime.now().plusDays(1).withHour(18).withMinute(0)
                    )
            );


            AppointmentCreateRequest request = AppointmentCreateRequest.of(appointmentName, category, duration, dateTimeRequests);

            // When
            Member host = findHostById(savedMemberId);
            Group group  = findGroupById(savedGroupId);
            List<AppointmentDateTime> appointmentDateTimes = request.appointmentDateTimes();
            Appointment appointment = createAppointmentEntity(host, group, appointmentDateTimes, request);
            saveAppointment(appointment);
            AppointmentCreateResponse appointmentCreateResponse = buildAppointmentCreateResponse(appointment, appointmentDateTimes);

            // Then
            assertThat(appointmentCreateResponse.appointmentName()).isEqualTo(appointmentName);
            assertThat(appointmentCreateResponse.category()).isEqualTo(category);
            assertThat(appointmentCreateResponse.duration()).isEqualTo(duration);
            assertThat(appointmentCreateResponse.appointmentDateTimes()).hasSize(2);
        }
    }

    @ParameterizedTest
    @DisplayName("약속 생성 실패 - 존재하지 않는 그룹")
    @CsvSource({
            "999",
            "1000",
            "1001"
    })
    void shouldFailToCreateAppointmentWhenGroupDoesNotExist(Long invalidGroupId) {
        // Given
        String appointmentName = "Study Group";
        String category = "중요";
        int duration = 120;
        List<AppointmentDateTime> appointmentDateTimes = List.of(
                AppointmentDateTime.of(
                        LocalDateTime.now(),
                        LocalDateTime.now().withHour(10).withMinute(0),
                        LocalDateTime.now().withHour(12).withMinute(0)
                )
        );
        AppointmentCreateRequest request = AppointmentCreateRequest.of(
                appointmentName, category, duration, appointmentDateTimes
        );

        // When & Then
        assertThatThrownBy(() -> {
            Member host = findHostById(savedMemberId);
            Group group = findGroupById(invalidGroupId);
            List<AppointmentDateTime> appointmentDateTimesList = request.appointmentDateTimes();
            Appointment appointment = createAppointmentEntity(host, group, appointmentDateTimesList, request);
            saveAppointment(appointment);
        })
                .isInstanceOf(AppointmentException.class)
                .hasMessage(AppointmentErrorCode.GROUP_NOT_FOUND.getMessage());
    }

    @ParameterizedTest
    @DisplayName("약속 생성 실패 - 존재하지 않는 사용자")
    @CsvSource({
            "999",
            "1000",
            "1001"
    })
    void shouldFailToCreateAppointmentWhenUserDoesNotExist(Long invalidUserId) {
        // Given
        String appointmentName = "Study Group";
        String category = "중요";
        int duration = 120;
        List<AppointmentDateTime> appointmentDateTimes = List.of(
                AppointmentDateTime.of(
                        LocalDateTime.now(),
                        LocalDateTime.now().withHour(10).withMinute(0),
                        LocalDateTime.now().withHour(12).withMinute(0)
                )
        );
        AppointmentCreateRequest request = AppointmentCreateRequest.of(
                appointmentName, category, duration, appointmentDateTimes
        );

        // When & Then
        assertThatThrownBy(() -> {
            Member host = findHostById(invalidUserId);
            Group group = findGroupById(savedGroupId);
            List<AppointmentDateTime> appointmentDateTimesList = request.appointmentDateTimes();
            Appointment appointment = createAppointmentEntity(host, group, appointmentDateTimesList, request);
            saveAppointment(appointment);
        })
                .isInstanceOf(AppointmentException.class)
                .hasMessage(AppointmentErrorCode.MEMBER_NOT_FOUND.getMessage());
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

    private Member findHostById(Long userId) {
        return memberRepository.findById(userId)
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
