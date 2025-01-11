package org.noostak.server.group.application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.noostak.server.group.domain.Group;
import org.noostak.server.group.domain.GroupRepositoryTest;
import org.noostak.server.group.domain.vo.GroupImageUrl;
import org.noostak.server.group.domain.vo.GroupMemberCount;
import org.noostak.server.group.domain.vo.GroupName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class GroupServiceTest {

    @Autowired
    private GroupRepositoryTest groupRepository;

    @BeforeEach
    void setUp() {
        groupRepository.deleteAll();

        String groupOneImageUrl = "https://bucket-noostak.s3.ap-northeast-2.amazonaws.com/images/group1.png";
        String groupTwoImageUrl = "https://bucket-noostak.s3.ap-northeast-2.amazonaws.com/images/group2.png";

        String groupOneInviteCode = "123ABC";
        String groupTwoInviteCode = "ABC456";

        groupRepository.save(
                Group.of(
                        GroupName.from("Group 1"),
                        GroupImageUrl.from(groupOneImageUrl),
                        GroupMemberCount.from(10L),
                        groupOneInviteCode
                )
        );

        groupRepository.save(
                Group.of(
                        GroupName.from("Group 2"),
                        GroupImageUrl.from(groupTwoImageUrl),
                        GroupMemberCount.from(20L),
                        groupTwoInviteCode
                )
        );
    }


    @Test
    @DisplayName("전체 그룹 목록을 성공적으로 조회")
    void shouldGetAllGroups() {
        // When
        List<Group> groups = groupRepository.findAll();

        // Then
        assertThat(groups).hasSize(2);
        assertThat(groups.get(0).getName().value()).isEqualTo("Group 1");
        assertThat(groups.get(1).getName().value()).isEqualTo("Group 2");
    }
}