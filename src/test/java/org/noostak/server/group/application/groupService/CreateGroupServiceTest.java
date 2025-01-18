package org.noostak.server.group.application.groupService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.noostak.server.group.application.GroupService;
import org.noostak.server.group.application.InviteCodeGenerator;
import org.noostak.server.group.common.GroupErrorCode;
import org.noostak.server.group.common.GroupException;
import org.noostak.server.group.domain.GroupInviteCode;
import org.noostak.server.group.domain.GroupRepositoryTest;
import org.noostak.server.group.dto.request.GroupCreateRequest;
import org.noostak.server.group.dto.response.GroupCreateResponse;
import org.noostak.server.infra.MockFileStorageService;
import org.noostak.server.member.domain.Member;
import org.noostak.server.member.domain.MemberRepositoryTest;
import org.noostak.server.member.domain.vo.*;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

public class CreateGroupServiceTest {

    private GroupRepositoryTest groupRepository;
    private MemberRepositoryTest memberRepository;
    private GroupService groupService;
    private Long savedMemberId;

    @Mock
    private InviteCodeGenerator inviteCodeGenerator;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        groupRepository = new GroupRepositoryTest();
        memberRepository = new MemberRepositoryTest();
        MockFileStorageService fileStorageService = new MockFileStorageService();

        groupService = new GroupService(groupRepository, memberRepository, inviteCodeGenerator, fileStorageService);

        groupRepository.deleteAll();
        memberRepository.deleteAll();

        Member savedMember = saveMember("firstMember",
                "https://noostak.s3.ap-northeast-2.amazonaws.com/group-images/1.jpg",
                "123456", "refreshToken1");
        savedMemberId = savedMember.getMemberId();
        Mockito.when(inviteCodeGenerator.generate()).thenReturn(GroupInviteCode.from("ABC123"));
    }


    @Nested
    @DisplayName("성공 케이스")
    class Success {

        @Test
        @DisplayName("그룹 생성 성공")
        void shouldCreateGroup() throws IOException {
            Long userId = savedMemberId;
            String groupName = "New Group";
            GroupCreateRequest request = GroupCreateRequest.of(groupName, null);

            GroupCreateResponse response = groupService.createGroup(userId, request);

            assertThat(response.groupName()).isEqualTo("New Group");
        }
    }

    @Nested
    @DisplayName("실패 케이스")
    class Failure {

        @ParameterizedTest
        @DisplayName("그룹 생성 실패 - 존재하지 않는 사용자")
        @CsvSource({"999", "1000", "1001"})
        void shouldFailToCreateGroupWhenUserDoesNotExist(Long invalidUserId) {
            String groupName = "Invalid Group";
            MockMultipartFile file = new MockMultipartFile(
                    "file",
                    "thumbnail.png",
                    MediaType.IMAGE_PNG_VALUE,
                    "thumbnail".getBytes()
            );
            GroupCreateRequest request = GroupCreateRequest.of(groupName, file);

            assertThatThrownBy(() -> groupService.createGroup(invalidUserId, request))
                    .isInstanceOf(GroupException.class)
                    .hasMessage(GroupErrorCode.LEADER_NOT_FOUND.getMessage());
        }
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
