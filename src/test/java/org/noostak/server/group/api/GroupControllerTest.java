package org.noostak.server.group.api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.noostak.server.group.domain.Group;
import org.noostak.server.group.domain.GroupRepository;
import org.noostak.server.group.domain.vo.GroupImageUrl;
import org.noostak.server.group.domain.vo.GroupMemberCount;
import org.noostak.server.group.domain.vo.GroupName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class GroupControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private GroupRepository groupRepository;

    @BeforeEach
    void setUp() {
        // 테스트 데이터 초기화
        groupRepository.deleteAll();
        groupRepository.save(Group.of(
                GroupName.from("Group 1"),
                GroupImageUrl.from("https://example.com/group1.png"),
                GroupMemberCount.from(10L),
                "123ABC"
        ));
        groupRepository.save(Group.of(
                GroupName.from("Group 2"),
                GroupImageUrl.from("https://example.com/group2.png"),
                GroupMemberCount.from(20L),
                "ABC456"
        ));
    }

    @Test
    @DisplayName("전체 그룹 목록을 성공적으로 조회")
    void shouldGetAllGroups() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/v1/groups"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("유저의 그룹 목록을 성공적으로 조회했습니다."))
                .andExpect(jsonPath("$.result.groups", hasSize(2)))
                .andExpect(jsonPath("$.result.groups[0].groupName").value("Group 1"))
                .andExpect(jsonPath("$.result.groups[1].groupName").value("Group 2"));
    }
}