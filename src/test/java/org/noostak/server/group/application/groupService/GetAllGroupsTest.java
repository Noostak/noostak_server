package org.noostak.server.group.application.groupService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.noostak.server.group.application.GroupService;
import org.noostak.server.group.domain.Group;
import org.noostak.server.group.domain.GroupRepositoryTest;
import org.noostak.server.group.domain.vo.GroupImageUrl;
import org.noostak.server.group.domain.vo.GroupName;
import org.noostak.server.group.dto.response.GroupsResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class GetAllGroupsTest {

    @Autowired
    private GroupRepositoryTest groupRepository;

    @Autowired
    private GroupService groupService;

    @BeforeEach
    void setUp() {
        groupRepository.deleteAll();

        saveGroup(1L, "Group 1", "https://bucket-noostak.s3.ap-northeast-2.amazonaws.com/images/group1.png", "123ABC");
        saveGroup(2L, "Group 2", "https://bucket-noostak.s3.ap-northeast-2.amazonaws.com/images/group2.png", "ABC456");
    }


    @Test
    @DisplayName("전체 그룹 목록을 성공적으로 조회")
    void shouldGetAllGroups() {
        // When
        GroupsResponse fetchedGroups = groupService.findAllGroups();

        // Then
        assertThat(fetchedGroups.groups()).hasSize(2);
        assertThat(fetchedGroups.groups().get(0).groupName()).isEqualTo("Group 1");
        assertThat(fetchedGroups.groups().get(1).groupName()).isEqualTo("Group 2");
    }

    private void saveGroup(Long leaderId, String groupName, String imageUrl, String inviteCode) {
        groupRepository.save(
                Group.of(
                        leaderId,
                        GroupName.from(groupName),
                        GroupImageUrl.from(imageUrl),
                        inviteCode
                )
        );
    }
}