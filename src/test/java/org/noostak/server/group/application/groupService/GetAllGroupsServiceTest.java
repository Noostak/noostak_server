package org.noostak.server.group.application.groupService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.noostak.server.group.domain.Group;
import org.noostak.server.group.domain.GroupRepository;
import org.noostak.server.group.domain.GroupRepositoryTest;
import org.noostak.server.group.domain.vo.GroupImageUrl;
import org.noostak.server.group.domain.vo.GroupName;
import org.noostak.server.group.dto.response.GroupResponse;
import org.noostak.server.group.dto.response.GroupsResponse;
import org.noostak.server.infra.FileStorageService;
import org.noostak.server.infra.MockFileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class GetAllGroupsServiceTest {

    private GroupRepositoryTest groupRepository;

    private FileStorageService fileStorageService;

    @BeforeEach
    void setUp() throws IOException {
        groupRepository = new GroupRepositoryTest();
        fileStorageService = new MockFileStorageService();

        groupRepository.deleteAll();

        saveGroup(1L, "Group 1", "group-images/group1.png", "123ABC");
        saveGroup(2L, "Group 2", "group-images/group2.png", "ABC456");
    }

    @Test
    @DisplayName("전체 그룹 목록을 성공적으로 조회")
    void shouldGetAllGroups() {
        // When
        List<GroupResponse> groups = groupRepository.findAll().stream()
                .map(GroupResponse::of)
                .toList();
        GroupsResponse groupsResponse = GroupsResponse.of(groups);

        // Then
        assertThat(groupsResponse.groups()).hasSize(2);
        assertThat(groupsResponse.groups().get(0).groupName()).isEqualTo("Group 1");
        assertThat(groupsResponse.groups().get(1).groupName()).isEqualTo("Group 2");
        assertThat(groupsResponse.groups().get(0).groupImageUrl()).isEqualTo("https://mock-url.com/test-image.png");
        assertThat(groupsResponse.groups().get(1).groupImageUrl()).isEqualTo("https://mock-url.com/test-image.png");
    }

    private void saveGroup(Long hostId, String groupName, String imagePath, String inviteCode) throws IOException {
        String uploadedImageUrl = fileStorageService.uploadImage("group-images", null);

        groupRepository.save(
                Group.of(
                        hostId,
                        GroupName.from(groupName),
                        GroupImageUrl.from(uploadedImageUrl),
                        inviteCode
                )
        );
    }
}
