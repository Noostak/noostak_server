package org.noostak.server.group.application;

import lombok.RequiredArgsConstructor;
import org.noostak.server.group.common.GroupErrorCode;
import org.noostak.server.group.common.GroupException;
import org.noostak.server.group.domain.Group;
import org.noostak.server.group.domain.GroupInviteCode;
import org.noostak.server.group.domain.GroupRepository;
import org.noostak.server.group.domain.vo.GroupImageUrl;
import org.noostak.server.group.domain.vo.GroupName;
import org.noostak.server.group.dto.request.GroupCreateRequest;
import org.noostak.server.group.dto.response.GroupCreateResponse;
import org.noostak.server.group.dto.response.GroupResponse;
import org.noostak.server.group.dto.response.GroupsResponse;
import org.noostak.server.infra.FileStorageService;
import org.noostak.server.infra.S3Service;
import org.noostak.server.member.domain.Member;
import org.noostak.server.member.domain.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GroupService {

    private final GroupRepository groupRepository;
    private final MemberRepository memberRepository;
    private final InviteCodeGenerator inviteCodeGenerator;
    private final FileStorageService fileStorageService;

    @Transactional
    public GroupCreateResponse createGroup(Long userId, GroupCreateRequest request) throws IOException {
        Member groupHost = memberRepository.findById(userId)
                .orElseThrow(() -> new GroupException(GroupErrorCode.LEADER_NOT_FOUND));

        GroupInviteCode inviteCode = inviteCodeGenerator.generate();

        String groupImageUrl = fileStorageService.uploadImage("group-images/", request.file());

        String groupName = request.groupName();

        Group group = Group.of(
                groupHost.getMemberId(),
                GroupName.from(groupName),
                GroupImageUrl.from(groupImageUrl),
                inviteCode.value()
        );

        groupRepository.save(group);

        return GroupCreateResponse.of(group);
    }

    public GroupsResponse findAllGroups() {
        List<GroupResponse> groups = groupRepository.findAll().stream()
                .map(GroupResponse::of)
                .toList();
        return GroupsResponse.of(groups);
    }
}
