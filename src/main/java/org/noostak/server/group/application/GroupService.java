package org.noostak.server.group.application;

import lombok.RequiredArgsConstructor;
import org.noostak.server.group.domain.Group;
import org.noostak.server.group.domain.GroupRepository;
import org.noostak.server.group.domain.vo.GroupImageUrl;
import org.noostak.server.group.domain.vo.GroupMemberCount;
import org.noostak.server.group.domain.vo.GroupName;
import org.noostak.server.group.dto.response.GroupResponse;
import org.noostak.server.group.dto.response.GroupsResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GroupService {

    private final GroupRepository groupRepository;

    public GroupsResponse findAllGroups() {
        List<GroupResponse> groups = groupRepository.findAll().stream()
                .map(GroupResponse::of)
                .toList();
        return GroupsResponse.of(groups);
    }
}
